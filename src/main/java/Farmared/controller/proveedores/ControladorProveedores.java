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
