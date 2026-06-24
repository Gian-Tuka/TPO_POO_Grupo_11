package Farmared.controller.item;

import Farmared.controller.proveedores.ControladorProveedores;
import Farmared.dto.item.ItemDTO;
import Farmared.dto.item.UnidadDeMedidaDTO;
import Farmared.dto.proveedor.ProveedorDTO;
import Farmared.exception.InvalidItemException;
import Farmared.exception.RubroNotExistException;
import Farmared.exception.UDMException;
import Farmared.model.item.*;
import Farmared.model.precio.PrecioProveedor;
import Farmared.model.proveedor.Proveedor;
import Farmared.model.rubro.Rubro;
import Farmared.utils.Validations;
import java.util.ArrayList;
import java.util.Date;

public class ControladorProductosYServicios {
    private static ControladorProductosYServicios instance = null;

    private ArrayList<Item> items;
    private ArrayList<UnidadDeMedida> unidadesDeMedida;

    private ControladorProductosYServicios() {
        this.items = new ArrayList<Item>();
        this.unidadesDeMedida = new ArrayList<UnidadDeMedida>();
    }

    public synchronized static ControladorProductosYServicios getInstance() {

        if (instance == null) {
            instance = new ControladorProductosYServicios();
        }
        return instance;
    }

    // Alta de Item desde la Vista
    public ItemDTO registrarItem(ItemDTO dto) throws Exception {

        //TODO: Validaciones
        Item nuevoItem = toModel(dto);

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

    // comunicación para el Controlador de Proveedores
    public Item buscarItemModeloPorCodigo(String codigo) {

        for (int  i = 0; i < items.size(); i++) {
            if(items.get(i).getCodigo().equals(codigo)) {
                return items.get(i);
            }
        }
        return null;
    }

    public void registrarUnidadDeMedida(UnidadDeMedida udm) {
        if (udm != null) {
            this.unidadesDeMedida.add(udm);
        }
    }

    private UnidadDeMedida buscarUnidadModelo(String codigo) {

        for  (int  i = 0; i < unidadesDeMedida.size(); i++) {
            if(unidadesDeMedida.get(i).getCodigoUnidad().equals(codigo)) {
                return unidadesDeMedida.get(i);
            }
        }
        return null;
    }

    private ItemDTO toDTO(Item item) {
        String precioVigente = "Sin precio";

        ArrayList<PrecioProveedor> precios = item.getPrecioItem();
        if (precios != null && !precios.isEmpty()) {
            float ultimo = precios.get(precios.size() - 1).getPrecioItem();
            precioVigente = String.valueOf(ultimo);
        }

        ItemDTO dto = new ItemDTO(
                item.getCodigo(),
                item.getDescripcionDeItem(),
                item.getUnidadMedida().getDescripcionUnidad(),
                item.getUnidadMedida().getCodigoUnidad(),
                item.getTipoDeIVA().name(),
                item.getRubro().getNombreRubro(),
                precioVigente,
                item.isActivo()
        );
        
        if (item instanceof Producto) {
            dto.setTipoItem("PRODUCTO");
        } else if (item instanceof Servicio) {
            dto.setTipoItem("SERVICIO");
        }
        
        return dto;
    }

    public Item toModel(ItemDTO dto) throws Exception {
        Validations v = new Validations();


        UnidadDeMedida udm = buscarUnidadModelo(dto.getTipoUDM());
        if (udm == null) {
            throw new UDMException("Unidad de medida seleccionada no existe.");
        }

        TipoDeIVA enumIVA = TipoDeIVA.valueOf(dto.getTipoDeIVA());

        Rubro rubroModelo = ControladorProveedores.getInstance().buscarRubroPorNombre(dto.getRubro());
        if (rubroModelo == null) {
            throw new RubroNotExistException("El Rubro seleccionado no existe.");
        }

        Item itemResultado;
        if ("PRODUCTO".equalsIgnoreCase(dto.getTipoItem())) {
            itemResultado = new Producto(dto.getDescripcionDeItem(), udm, enumIVA, rubroModelo);
        } else if ("SERVICIO".equalsIgnoreCase(dto.getTipoItem())) {
            itemResultado = new Servicio(dto.getDescripcionDeItem(), udm, enumIVA, rubroModelo);
        } else {
            throw new InvalidItemException("Tipo de ítem inválido.");
        }

        return itemResultado;
    }

    public ArrayList<ItemDTO> obtenerItemsDTO() {
        ArrayList<ItemDTO> itemsDTO = new ArrayList<>();
        for (Item item : items) {
            if (item.isActivo()) {
                itemsDTO.add(toDTO(item));
            }
        }
        return itemsDTO;
    }

    public ArrayList<UnidadDeMedidaDTO> obtenerUnidadesDeMedidaDTO() {
        ArrayList<UnidadDeMedidaDTO> udmDTOs = new ArrayList<>();
        for (UnidadDeMedida udm : unidadesDeMedida) {
            udmDTOs.add(new UnidadDeMedidaDTO(udm.getCodigoUnidad(), udm.getDescripcionUnidad(), udm.getTipoDeUnidad().name()));
        }
        return udmDTOs;
    }

    public UnidadDeMedidaDTO altaUnidadDeMedida(UnidadDeMedidaDTO dto) {
        TipoDeUnidad tipo = TipoDeUnidad.valueOf(dto.getTipoDeUnidad());
        UnidadDeMedida nuevaUdm = new UnidadDeMedida(dto.getDescripcionUnidad(), tipo);
        unidadesDeMedida.add(nuevaUdm);
        return new UnidadDeMedidaDTO(nuevaUdm.getCodigoUnidad(), nuevaUdm.getDescripcionUnidad(), nuevaUdm.getTipoDeUnidad().name());
    }

    public void modificarUnidadDeMedida(UnidadDeMedidaDTO dto) throws Exception {
        UnidadDeMedida udm = buscarUnidadModelo(dto.getCodigoUnidad());
        if (udm != null) {
            udm.setDescripcionUnidad(dto.getDescripcionUnidad());
            udm.setTipoDeUnidad(TipoDeUnidad.valueOf(dto.getTipoDeUnidad()));
        } else {
            throw new Exception("Unidad de medida no encontrada");
        }
    }

    public void eliminarUnidadDeMedida(String codigo) throws Exception {
        UnidadDeMedida udm = buscarUnidadModelo(codigo);
        if (udm != null) {
            unidadesDeMedida.remove(udm);
        } else {
            throw new Exception("Unidad de medida no encontrada");
        }
    }

    public void modificarItem(ItemDTO dto) throws Exception {
        Item item = buscarItemModeloPorCodigo(dto.getCodigo());
        if (item == null) {
            throw new InvalidItemException("Item no encontrado.");
        }
        
        item.setDescripcionDeItem(dto.getDescripcionDeItem());
        
        UnidadDeMedida udm = buscarUnidadModelo(dto.getTipoUDM());
        if (udm != null) {
            item.setUnidadMedida(udm);
        }

        item.setTipoDeIVA(TipoDeIVA.valueOf(dto.getTipoDeIVA()));

        Rubro rubroModelo = ControladorProveedores.getInstance().buscarRubroPorNombre(dto.getRubro());
        if (rubroModelo != null) {
            item.setRubro(rubroModelo);
        }
    }

    public void eliminarItem(String codigo) throws Exception {
        Item item = buscarItemModeloPorCodigo(codigo);
        if (item != null) {
            item.setActivo(false);
        } else {
            throw new InvalidItemException("Item no encontrado.");
        }
    }

    public String[] obtenerTiposDeIva() {
        TipoDeIVA[] values = TipoDeIVA.values();
        String[] nombres = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            nombres[i] = values[i].name();
        }
        return nombres;
    }

    public String[] obtenerTiposDeUnidad() {
        TipoDeUnidad[] values = TipoDeUnidad.values();
        String[] nombres = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            nombres[i] = values[i].name();
        }
        return nombres;
    }
}
