package Farmared.controller.proveedores;

import Farmared.controller.item.ControladorProductosYServicios;
import Farmared.dto.proveedor.ProveedorDTO;
import Farmared.dto.rubro.RubroDTO;
import Farmared.model.item.Item;
import Farmared.model.precio.PrecioProveedor;
import Farmared.model.proveedor.CondicionIVA;
import Farmared.model.proveedor.Proveedor;
import Farmared.model.rubro.Rubro;
import Farmared.model.rubro.TipoRubro;
import Farmared.model.user.Area;
import Farmared.model.user.Rol;
import Farmared.model.user.Usuario;
import Farmared.utils.Domicilio;
import Farmared.utils.UtilDate;

import java.util.ArrayList;
import java.util.Date;

public class ControladorProveedores {

    private static ControladorProveedores instance = null;

    private ArrayList<Proveedor> proveedores;
    private ArrayList<Rubro> rubrosGlobales;

    private ControladorProveedores() {
        this.proveedores = new ArrayList<Proveedor>();
        this.rubrosGlobales = new ArrayList<Rubro>();
        cargarDatosSimulados();

        // 2. ADENTRO del constructor sí podés usar el .add() sin errores:
        Domicilio domicilio = new Domicilio("Av. Falsa", "123", "1234", "Springfield", "Argentina");
        Date miFecha = UtilDate.stringToDate("01/01/2000");

        Proveedor proveedor1 = new Proveedor("30-00000000-1", "Gomez S.A", "Gomesitos", domicilio, "12344321",
                "gomesitos@gomez.com.ar", CondicionIVA.RESPONSABLE_INSCRIPTO, "888888888", miFecha, 10000000);

        Proveedor proveedor2 = new Proveedor("30-00000000-2", "Zemog S.A", "Random", domicilio, "99988877",
                "zemog5@gomez.com.ar", CondicionIVA.RESPONSABLE_INSCRIPTO, "888888888", miFecha, 10000000);

        proveedores.add(proveedor1);
        proveedores.add(proveedor2);
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
            throw new Exception("Ya existe un proveedor registrado con el CUIT: " + dto.getCuit());
        }

        Proveedor nuevo = toModel(dto);

        // vinculamos los Rubros que el usuario puso en la GUI
        for (String nombreRubro : dto.getIdsRubros()) {
            Rubro r = buscarRubroPorNombre(nombreRubro);
            if (r != null) {
                nuevo.getRubroProveedor().add(r);
            }
        }

        this.proveedores.add(nuevo);
        return toDTO(nuevo);
    }

    public ProveedorDTO modificarProveedor(ProveedorDTO dto) throws Exception {
        Proveedor proveedor = buscarProveedorPorCuit(dto.getCuit());
        if (proveedor == null) {
            throw new Exception("No existe un proveedor registrado con el CUIT: " + dto.getCuit());
        }

        proveedor.setRazonSocial(dto.getRazonSocial());
        proveedor.setNombreFantasia(dto.getNombreFantasia());
        proveedor.setDomicilioComercial(new Domicilio(dto.getCalle(), dto.getNumeroDpto(), dto.getCodigoPostal(), dto.getCiudad(), dto.getPais()));
        proveedor.setTelefono(dto.getTelefono());
        proveedor.setCorreo(dto.getCorreo());
        proveedor.setCondicionIVA(CondicionIVA.valueOf(dto.getCondicionIVA()));
        proveedor.setNroIngBru(dto.getNroIngBru());
        proveedor.getCuentaCorriente().getTopeDeuda();

        ArrayList<Rubro> nuevosRubros = new ArrayList<>();
        for (String nombreRubro : dto.getIdsRubros()) {
            Rubro r = buscarRubroPorNombre(nombreRubro);
            if (r != null) {
                nuevosRubros.add(r);
            }
        }
        proveedor.setRubroProveedor(nuevosRubros);

        return toDTO(proveedor);
    }

    public void eliminarProveedor(String cuit) throws Exception {
        Proveedor proveedor = buscarProveedorPorCuit(cuit);
        if (proveedor == null) {
            throw new Exception("No existe un proveedor registrado con el CUIT: " + cuit);
        }
        this.proveedores.remove(proveedor);
    }

    public ProveedorDTO buscarProveedorDTOPorCuit(String cuit) {
        Proveedor proveedor = buscarProveedorPorCuit(cuit);
        if (proveedor != null) {
            return toDTO(proveedor);
        }
        return null;
    }

    public ArrayList<ProveedorDTO> obtenerProveedoresDTO() {
        ArrayList<ProveedorDTO> lista = new ArrayList<>();
        for (Proveedor p : proveedores) {
            lista.add(toDTO(p));
        }
        return lista;
    }

    private void cargarDatosSimulados() {
        if (this.rubrosGlobales.isEmpty()) {
            this.rubrosGlobales.add(new Rubro("Medicamentos", TipoRubro.BIENES));
            this.rubrosGlobales.add(new Rubro("Higiene", TipoRubro.BIENES));
            this.rubrosGlobales.add(new Rubro("Limpieza", TipoRubro.SERVICIOS));
            this.rubrosGlobales.add(new Rubro("Mantenimiento", TipoRubro.SERVICIOS));
        }

        if (this.proveedores.isEmpty()) {
            try {
                ProveedorDTO dto1 = new ProveedorDTO("30-12345678-1", "Proveedor Alfa S.A.", "Alfa", "Calle Falsa", "123", "1000", "CABA", "Argentina", "4555-1234", "alfa@test.com", "RESPONSABLE_INSCRIPTO", "12345", "", 100000f, new ArrayList<>());
                ProveedorDTO dto2 = new ProveedorDTO("30-87654321-0", "Distribuidora Beta SRL", "Beta", "Avenida Siempreviva", "742", "1000", "CABA", "Argentina", "4555-5678", "beta@test.com", "MONOTRIBUTISTA", "54321", "", 50000f, new ArrayList<>());
                ProveedorDTO dto3 = new ProveedorDTO("27-11223344-5", "Logística Gamma", "Gamma", "Ruta 9", "Km 50", "1629", "Pilar", "Argentina", "4555-9012", "gamma@test.com", "EXENTO", "11223", "", 25000f, new ArrayList<>());

                this.proveedores.add(toModel(dto1));
                this.proveedores.add(toModel(dto2));
                this.proveedores.add(toModel(dto3));
            } catch (Exception e) {
                // Ignore errors during simulation loading
            }
        }
    }

    // REGISTRO DE PRECIO: Doble amarre usando consistencia bidireccional
    public void registrarPrecioProveedor(String cuitProveedor, String codigoItem, float valorPrecio) {
        Proveedor prov = buscarProveedorPorCuit(cuitProveedor);

        // COMUNICACIÓN INTER-CONTROLADOR: Llamada al Singleton de Items para obtener el modelo real
        Item item = ControladorProductosYServicios.getInstance().buscarItemModeloPorCodigo(codigoItem);

        if (prov == null) throw new RuntimeException("Error: No se encontró el Proveedor especificado.");
        if (item == null) throw new RuntimeException("Error: No se encontró el Artículo o Servicio.");

        // Construimos la clase intermedia de asociación
        PrecioProveedor nuevoPrecio = new PrecioProveedor(item, prov, valorPrecio, new Date());

        // Ejecutamos la consistencia bidireccional en las estructuras ArrayList
        prov.getPrecioPorItem().add(nuevoPrecio);
        item.getPrecioItem().add(nuevoPrecio);
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

    private static ProveedorDTO toDTO(Proveedor model) {
        ArrayList<String> nombresRubros = new ArrayList<>();
        for (Rubro r : model.getRubroProveedor()) {
            nombresRubros.add(r.getNombreRubro());
        }

        //parseo a string la fecha inicio actividades
        UtilDate utilDate = new UtilDate();
        String fechaStr = utilDate.parseDate(model.getFechaInicioActividades());

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
    public ArrayList<ProveedorDTO> obtenerProveedores () {
        ArrayList<ProveedorDTO> listaProveedoresDTO = new ArrayList<>();
        for (Proveedor p : proveedores) {
            listaProveedoresDTO.add(toDTO(p));
        }
        return listaProveedoresDTO;
    }
    private static RubroDTO toDTORubro(Rubro model) {
        return new RubroDTO(
                model.getIdRubro(),
                model.getNombreRubro(),
                model.getTipoRubro().name()
        );
    }
    private static Proveedor toModel (ProveedorDTO dto) {
        Domicilio domicilio = new Domicilio(dto.getCalle(),
                dto.getNumeroDpto(),
                dto.getCodigoPostal(),
                dto.getCiudad(),
                dto.getPais()
        );

        return new Proveedor(
                dto.getCuit(),
                dto.getRazonSocial(),
                dto.getNombreFantasia(),
                domicilio,
                dto.getTelefono(),
                dto.getCorreo(),
                CondicionIVA.valueOf(dto.getCondicionIVA()),
                dto.getNroIngBru(),
                new Date(),
                dto.getTopeDeuda()
        );

    }

}
