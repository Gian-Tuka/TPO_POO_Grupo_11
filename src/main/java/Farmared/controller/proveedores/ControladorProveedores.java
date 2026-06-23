package Farmared.controller.proveedores;

import Farmared.controller.item.ControladorProductosYServicios;
import Farmared.controller.ordenes.ControladorDeOrdenDeCompra;
import Farmared.dto.proveedor.ProveedorDTO;
import Farmared.dto.rubro.RubroDTO;
import Farmared.exception.FarmaredException;
import Farmared.exception.ProveedorNoEncontradoException;
import Farmared.model.item.Item;
import Farmared.model.precio.PrecioProveedor;
import Farmared.model.proveedor.CondicionIVA;
import Farmared.model.proveedor.Proveedor;
import Farmared.model.rubro.Rubro;
import Farmared.model.rubro.TipoRubro;
import Farmared.model.user.Area;
import Farmared.utils.Domicilio;
import Farmared.utils.UtilDate;
import Farmared.utils.Validations;
import java.util.ArrayList;
import java.util.Date;

public class ControladorProveedores {

    private static ControladorProveedores instance = null;

    private ArrayList<Proveedor> proveedores;
    private ArrayList<Rubro> rubrosGlobales;

    private ControladorProveedores() {
        this.proveedores = new ArrayList<Proveedor>();
        this.rubrosGlobales = new ArrayList<Rubro>();
    }

    public synchronized static ControladorProveedores getInstance() {
        if (instance == null) {
            instance = new ControladorProveedores();
        }
        return instance;
    }

    // Alta de Proveedor
    public ProveedorDTO registrarProveedor(ProveedorDTO dto) throws Exception {

        if (buscarProveedorPorCuit(dto.getCuit()) != null) {
            throw new FarmaredException("Ya existe un proveedor registrado con el CUIT: " + dto.getCuit());
        }

        Proveedor nuevo = toModelProveedor(dto);

        // vinculamos los Rubros que el usuario puso en la GUI
        for (String nombreRubro : dto.getIdsRubros()) {
            Rubro r = buscarRubroPorNombre(nombreRubro);
            if (r != null) {
                nuevo.asociarRubro(r); // Bug 34 fix: usar método encapsulado
            }
        }

        this.proveedores.add(nuevo);
        return toDTOProveedor(nuevo);
    }

    // Bug 1 — modificarProveedor() ahora escribe el tope de deuda con setTopeDeuda()
    public ProveedorDTO modificarProveedor(ProveedorDTO dto) {
        Proveedor proveedor = buscarProveedorPorCuit(dto.getCuit());
        if (proveedor == null) {
            throw new ProveedorNoEncontradoException(dto.getCuit());
        }

        proveedor.setRazonSocial(dto.getRazonSocial());
        proveedor.setNombreFantasia(dto.getNombreFantasia());
        proveedor.setDomicilioComercial(new Domicilio(dto.getCalle(), dto.getNumeroDpto(), dto.getCodigoPostal(), dto.getCiudad(), dto.getPais()));
        proveedor.setTelefono(dto.getTelefono());
        proveedor.setCorreo(dto.getCorreo());
        proveedor.setCondicionIVA(CondicionIVA.valueOf(dto.getCondicionIVA()));
        proveedor.setNroIngBru(dto.getNroIngBru());
        proveedor.getCuentaCorriente().setTopeDeuda(dto.getTopeDeuda()); // Bug 1 fix

        ArrayList<Rubro> nuevosRubros = new ArrayList<>();
        for (String nombreRubro : dto.getIdsRubros()) {
            Rubro r = buscarRubroPorNombre(nombreRubro);
            if (r != null) {
                nuevosRubros.add(r);
            }
        }
        proveedor.setRubroProveedor(nuevosRubros);

        return toDTOProveedor(proveedor);
    }

    // Bug 39 — eliminarProveedor() verifica referencias activas antes de eliminar
    public void eliminarProveedor(String cuit) {
        Proveedor prov = buscarProveedorPorCuit(cuit);
        if (prov == null) {
            throw new ProveedorNoEncontradoException(cuit);
        }

        // Verificar OC activas
        ControladorDeOrdenDeCompra ctrlOC = ControladorDeOrdenDeCompra.getInstance();
        boolean tieneOCActivas = ctrlOC.tieneOrdenesActivas(cuit);
        if (tieneOCActivas) {
            throw new FarmaredException("No se puede eliminar: el proveedor tiene OC activas");
        }

        // Verificar deuda pendiente
        if (prov.getCuentaCorriente().getDeudaActual() > 0) {
            throw new FarmaredException("No se puede eliminar: el proveedor tiene deuda pendiente");
        }

        // Limpiar precios asociados
        for (PrecioProveedor pp : new ArrayList<>(prov.getPrecioPorItem())) {
            pp.getItem().eliminarPrecio(pp);
        }
        proveedores.remove(prov);
    }

    public ProveedorDTO buscarProveedorDTOPorCuit(String cuit) {
        Proveedor proveedor = buscarProveedorPorCuit(cuit);
        if (proveedor != null) {
            return toDTOProveedor(proveedor);
        }
        return null;
    }

    // Método público para uso inter-controlador
    public Proveedor buscarProveedorModelo(String cuit) {
        return buscarProveedorPorCuit(cuit);
    }

    public ArrayList<ProveedorDTO> obtenerProveedoresDTO() {
        ArrayList<ProveedorDTO> lista = new ArrayList<>();
        for (Proveedor p : proveedores) {
            lista.add(toDTOProveedor(p));
        }
        return lista;
    }

    private void cargarDatosSimulados() {
        if (this.proveedores.isEmpty()) {
            try {
                ProveedorDTO dto1 = new ProveedorDTO("30-12345678-1", "Proveedor Alfa S.A.", "Alfa", "Calle Falsa", "123", "1000", "CABA", "Argentina", "4555-1234", "alfa@test.com", "RESPONSABLE_INSCRIPTO", "12345", "", 100000f, new ArrayList<>());
                ProveedorDTO dto2 = new ProveedorDTO("30-87654321-0", "Distribuidora Beta SRL", "Beta", "Avenida Siempreviva", "742", "1000", "CABA", "Argentina", "4555-5678", "beta@test.com", "MONOTRIBUTISTA", "54321", "", 50000f, new ArrayList<>());
                ProveedorDTO dto3 = new ProveedorDTO("27-11223344-5", "Logística Gamma", "Gamma", "Ruta 9", "Km 50", "1629", "Pilar", "Argentina", "4555-9012", "gamma@test.com", "EXENTO", "11223", "", 25000f, new ArrayList<>());
                
                this.proveedores.add(toModelProveedor(dto1));
                this.proveedores.add(toModelProveedor(dto2));
                this.proveedores.add(toModelProveedor(dto3));
            } catch (Exception e) {
                // Ignore errors during simulation loading
            }
        }
    }

    // Bug 38 — registrarPrecioProveedor() usa métodos encapsulados
    public void registrarPrecioProveedor(String cuitProveedor, String codigoItem, float valorPrecio) {
        Proveedor prov = buscarProveedorPorCuit(cuitProveedor);

        // COMUNICACIÓN INTER-CONTROLADOR: Llamada al Singleton de Items para obtener el modelo real
        Item item = ControladorProductosYServicios.getInstance().buscarItemModeloPorCodigo(codigoItem);

        Validations v = new Validations();
        v.requireNonNull(prov, "Error: No se encontró el Proveedor especificado.");
        v.requireNonNull(item, "Error: No se encontró el Artículo o Servicio.");

        // Construimos la clase intermedia de asociación
        PrecioProveedor nuevoPrecio = new PrecioProveedor(item, prov, valorPrecio, new Date());

        // Bug 38 fix: Usar métodos encapsulados en vez de acceso directo a listas internas
        prov.agregarPrecioItem(nuevoPrecio);
        item.agregarPrecio(nuevoPrecio);
    }

    // Búsqueda interna para uso del Controlador de Items
    public Rubro buscarRubroPorId(String id) {
        for (Rubro r : rubrosGlobales) {
            if (r.getIdRubro().equals(id)) {
                return r;
            }
        }
        return null;
    }

    private Proveedor buscarProveedorPorCuit(String cuit) {
        for (Proveedor p : proveedores) {
            if (p.getCuit().equals(cuit)) {
                return p;
            }
        }
        return null;
    }

    public boolean existeProveedor(Proveedor proveedor) {
        return proveedores.contains(proveedor);
    }

    public Farmared.model.cuentaCorriente.CuentaCorriente cuentaCorriente(Proveedor proveedor) {
        if (existeProveedor(proveedor)) {
            return proveedor.getCuentaCorriente();
        }
        return null;
    }

    private Rubro buscarRubroPorNombre(String nombre) {
        for (Rubro r : rubrosGlobales) {
            if (r.getNombreRubro().equals(nombre)) {
                return r;
            }
        }
        return null;
    }

    public RubroDTO altaRubro(RubroDTO dto) {
        Rubro nuevo = new Rubro(dto.getNombre(), TipoRubro.valueOf(dto.getTipoRubro()));
        rubrosGlobales.add(nuevo);
        return toDTORubro(nuevo);
    }

    public ArrayList<RubroDTO> obtenerRubrosDTO() {
        ArrayList<RubroDTO> listaRubrosDTO = new ArrayList<>();

        for (Rubro r : this.rubrosGlobales) {
            // Transformamos cada Modelo Rubro en un RubroDTO
            RubroDTO dto = new RubroDTO(
                    r.getIdRubro(),
                    r.getNombreRubro(),
                    r.getTipoRubro().name()
            );
            listaRubrosDTO.add(dto);
        }

        return listaRubrosDTO;
    }

    public ArrayList<String> obtenerNombresRubros() {

        ArrayList<String> nombresRubros = new ArrayList<>();
        for (int i = 0; i < rubrosGlobales.size(); i++) {
            nombresRubros.add(rubrosGlobales.get(i).getNombreRubro());
        }

        return nombresRubros;
    }

    private static ProveedorDTO toDTOProveedor(Proveedor model) {
        ArrayList<String> nombresRubros = new ArrayList<>();
        for (Rubro r : model.getRubroProveedor()) {
            nombresRubros.add(r.getNombreRubro());
        }

        // Anti-patrón fix: llamada estática directa
        String fechaStr = UtilDate.parseDate(model.getFechaInicioActividades());

        return new ProveedorDTO(
                model.getCuit(),
                model.getRazonSocial(),
                model.getNombreFantasia(),
                model.getDomicilioComercial().getCalle(),
                model.getDomicilioComercial().getNumero(),
                model.getDomicilioComercial().getCodigoPostal(),
                model.getDomicilioComercial().getCiudad(),
                model.getDomicilioComercial().getPais(),
                model.getTelefono(),
                model.getCorreo(),
                model.getCondicionIVA().name(),
                model.getNroIngBru(),
                fechaStr,
                model.getCuentaCorriente().getTopeDeuda(),
                nombresRubros
        );
    }

    private static RubroDTO toDTORubro(Rubro model) {
        return new RubroDTO(
                model.getIdRubro(),
                model.getNombreRubro(),
                model.getTipoRubro().name()
        );
    }

    private static Rubro toModelRubro(RubroDTO dto) {
        return new Rubro(
                dto.getNombre(),
                TipoRubro.valueOf(dto.getTipoRubro())
        );
    }
    private static Proveedor toModelProveedor(ProveedorDTO dto) {
        Domicilio domicilio = new Domicilio(dto.getCalle(),
                dto.getNumeroDpto(),
                dto.getCodigoPostal(),
                dto.getCiudad(),
                dto.getPais()
        );

        // Bug 13 fix: parsear la fecha del DTO en vez de usar new Date()
        Date fechaInicioActividades;
        if (dto.getFechaInicioActividades() != null && !dto.getFechaInicioActividades().isEmpty()) {
            fechaInicioActividades = UtilDate.toDate(dto.getFechaInicioActividades());
        } else {
            fechaInicioActividades = new Date(); // Fallback para datos simulados sin fecha
        }

        return new Proveedor(
                dto.getCuit(),
                dto.getRazonSocial(),
                dto.getNombreFantasia(),
                domicilio,
                dto.getTelefono(),
                dto.getCorreo(),
                CondicionIVA.valueOf(dto.getCondicionIVA()),
                dto.getNroIngBru(),
                fechaInicioActividades,
                dto.getTopeDeuda()
        );

    }

}
