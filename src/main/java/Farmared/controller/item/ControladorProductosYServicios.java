package Farmared.controller.item;

import Farmared.controller.proveedores.ControladorProveedores;
import Farmared.dto.item.ItemDTO;
import Farmared.model.item.*;
import Farmared.model.precio.PrecioProveedor;
import Farmared.model.rubro.Rubro;

import java.util.ArrayList;

public class ControladorProductosYServicios {
    private static ControladorProductosYServicios instance = null;

    private ArrayList<Item> items;
    private ArrayList<UnidadDeMedida> unidadesDeMedida;

    private ControladorProductosYServicios() {
        this.items = new ArrayList<Item>();
        this.unidadesDeMedida = new ArrayList<UnidadDeMedida>();
        cargarUnidadesSimuladas();
    }

    public synchronized static ControladorProductosYServicios getInstance() {

        if (instance == null) {
            instance = new ControladorProductosYServicios();
        }
        return instance;
    }

    // Alta de Item desde la Vista
    public ItemDTO registrarItem(String descripcion, String codigoUnidad, String iva, String idRubro, String tipoItem) {

        Rubro rubroAsociado = ControladorProveedores.getInstance().buscarRubroPorId(idRubro);

        if (rubroAsociado == null) {
            throw new RuntimeException("Error: El rubro seleccionado no existe.");
        }

        UnidadDeMedida unidad = buscarUnidadModelo(codigoUnidad);
        if (unidad == null) {
            throw new RuntimeException("Error: Unidad de medida no válida.");
        }

        Item nuevoItem;
        TipoDeIVA tipoIVA = TipoDeIVA.valueOf(iva);

        if ("PRODUCTO".equalsIgnoreCase(tipoItem)) {
            // Pasamos una lista vacía de precios al constructor como pide tu modelo
            nuevoItem = new Producto(descripcion, unidad, new ArrayList<>(), tipoIVA, rubroAsociado);
        } else {
            nuevoItem = new Servicio(descripcion, unidad, new ArrayList<>(), tipoIVA, rubroAsociado);
        }

        items.add(nuevoItem);
        return toDTO(nuevoItem);
    }

    // Filtrar productos para la UI
    public ArrayList<ItemDTO> obtenerSoloProductos() {
        ArrayList<ItemDTO> itemDTOS = new ArrayList<ItemDTO>();

        for  (int i = 0; i < items.size(); i++) {
            if(items.get(i) instanceof Producto) {
                itemDTOS.add(toDTO(items.get(i)));
            }
        }

        return itemDTOS;
    }

    // Filtrar servicios para la UI
    public ArrayList<ItemDTO> obtenerSoloServicios() {
        ArrayList<ItemDTO> itemDTOS = new ArrayList<ItemDTO>();
        for  (int i = 0; i < items.size(); i++) {
            if(items.get(i) instanceof Servicio) {
                itemDTOS.add(toDTO(items.get(i)));
            }
        }
        return itemDTOS;
    }

    // Método de comunicación clave utilizado por el Controlador de Proveedores
    public Item buscarItemModeloPorCodigo(String codigo) {

        for (int  i = 0; i < items.size(); i++) {
            if(items.get(i).getCodigo().equals(codigo)) {
                return items.get(i);
            }
        }
        return null;
    }

    public UnidadDeMedida buscarUnidadModelo(String codigo) {
        for  (int  i = 0; i < unidadesDeMedida.size(); i++) {
            if(unidadesDeMedida.get(i).getCodigoUnidad().equals(codigo)) {
                return unidadesDeMedida.get(i);
            }
        }
        return null;
    }

    public ArrayList<String> obtenerDescripcionesUnidades() {
        ArrayList<String> descs = new ArrayList<>();
        for (UnidadDeMedida u : unidadesDeMedida) {
            descs.add(u.getDescripcionUnidad() + " (" + u.getCodigoUnidad() + ")");
        }
        return descs;
    }

    public String obtenerCodigoUnidadPorDescripcionCombo(String comboItem) {
        if (comboItem == null) return null;
        for (UnidadDeMedida u : unidadesDeMedida) {
            if ((u.getDescripcionUnidad() + " (" + u.getCodigoUnidad() + ")").equals(comboItem)) {
                return u.getCodigoUnidad();
            }
        }
        return null;
    }

    private void cargarUnidadesSimuladas() {
        unidadesDeMedida.add(new UnidadDeMedida("Kilogramo", TipoDeUnidad.PESO));
        unidadesDeMedida.add(new UnidadDeMedida("Litro", TipoDeUnidad.VOLUMEN));
        unidadesDeMedida.add(new UnidadDeMedida("Unidad", TipoDeUnidad.UNIDAD));
        unidadesDeMedida.add(new UnidadDeMedida("Hora", TipoDeUnidad.TIEMPO));
        unidadesDeMedida.add(new UnidadDeMedida("Mensual", TipoDeUnidad.TIEMPO));
    }


    private ItemDTO toDTO(Item item) {
        String precioVigente = "Sin precio";

        ArrayList<PrecioProveedor> precios = item.getPrecioItem();
        if (precios != null && !precios.isEmpty()) {
            float ultimo = precios.get(precios.size() - 1).getPrecioItem();
            precioVigente = String.valueOf(ultimo);
        }

        return new ItemDTO(
                item.getCodigo(),
                item.getDescripcionDeItem(),
                item.getUnidadMedida().getDescripcionUnidad(),
                item.getTipoDeIVA().name(),
                item.getRubro().getNombreRubro(),
                precioVigente
        );
    }

}
