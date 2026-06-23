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
            throw new Exception("Ya existe un proveedor registrado con el CUIT: " + dto.getCuit());
        }

        Proveedor nuevo = toModelProveedor(dto);

        // vinculamos los Rubros que el usuario puso en la GUI
        for (String nombreRubro : dto.getIdsRubros()) {
            Rubro r = buscarRubroPorNombre(nombreRubro);
            if (r != null) {
                nuevo.getRubroProveedor().add(r);
            }
        }

        this.proveedores.add(nuevo);
        return toDTOProveedor(nuevo);
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

        return toDTOProveedor(proveedor);
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
            return toDTOProveedor(proveedor);
        }
        return null;
    }

    public ArrayList<ProveedorDTO> obtenerProveedoresDTO() {
        ArrayList<ProveedorDTO> lista = new ArrayList<>();
        for (Proveedor p : proveedores) {
            lista.add(toDTOProveedor(p));
        }
        return lista;
    }

    // REGISTRO DE PRECIO: Doble amarre usando consistencia bidireccional
    public void registrarPrecioProveedor(String cuitProveedor, String codigoItem, float valorPrecio) {
        Proveedor prov = buscarProveedorPorCuit(cuitProveedor);

        // COMUNICACIÓN INTER-CONTROLADOR: Llamada al Singleton de Items para obtener el modelo real
        Item item = ControladorProductosYServicios.getInstance().buscarItemModeloPorCodigo(codigoItem);

        Validations v = new Validations();
        v.requireNonNull(prov, "Error: No se encontró el Proveedor especificado.");
        v.requireNonNull(item, "Error: No se encontró el Artículo o Servicio.");

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

    private static ProveedorDTO toDTOProveedor(Proveedor model) {
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
