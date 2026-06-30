package Farmared.controller.item;

import Farmared.controller.proveedores.ControladorProveedores;
import Farmared.dto.item.ItemDTO;
import Farmared.dto.item.UnidadDeMedidaDTO;
import Farmared.exception.*;
import Farmared.model.item.*;
import Farmared.model.precio.PrecioProveedor;
import Farmared.model.rubro.Rubro;
import Farmared.dto.item.PrecioProveedorDTO;
import Farmared.utils.UtilDate;
import Farmared.utils.Validations;
import java.util.ArrayList;

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
        ControladorProveedores ctrlProv = ControladorProveedores.getInstance();

        Rubro rubroID = ctrlProv.buscarRubroPorNombre(dto.getRubro());
        UnidadDeMedida udm = buscarUnidadDeMedida(dto.getTipoUDM());
        if (udm == null) {
            throw new UDMException("UDM inexistente");
        }
        try {
            TipoDeIVA.valueOf(dto.getTipoDeIVA());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new InvalidIVAException("Tipo de iva incorrecto");
        }

        if (rubroID == null) {
            throw new RubroNotExistException("Rubro inexistente");
        }

        Item nuevoItem = toModelItem(dto);

        items.add(nuevoItem);
        return toDTOItem(nuevoItem);
    }

    public void modificarItem(ItemDTO dto) {
        Item item = buscarItem(dto.getCodigo());
        if (item == null) {
            throw new InvalidItemException("Item no encontrado.");
        }

        item.setDescripcionDeItem(dto.getDescripcionDeItem());

        UnidadDeMedida udm = buscarUnidadDeMedida(dto.getTipoUDM());
        if (udm != null) {
            item.setUnidadMedida(udm);
        }

        item.setTipoDeIVA(TipoDeIVA.valueOf(dto.getTipoDeIVA()));

        Rubro rubroModelo = ControladorProveedores.getInstance().buscarRubroPorId(dto.getRubro());
        if (rubroModelo != null) {
            item.setRubro(rubroModelo);
        }
    }

    public void eliminarItem(String codigo) {
        Item item = buscarItem(codigo);
        if (item != null) {
            item.setActivo(false);
        } else {
            throw new InvalidItemException("Item no encontrado.");
        }
    }

    public Item buscarItem(String codigo) {

        for (int  i = 0; i < items.size(); i++) {
            if(items.get(i).getCodigo().equals(codigo)) {
                return items.get(i);
            }
        }
        return null;
    }

    public ArrayList<ItemDTO> obtenerItemsDTO() {
        ArrayList<ItemDTO> itemsDTO = new ArrayList<>();
        for (Item item : items) {
            if (item.isActivo()) {
                itemsDTO.add(toDTOItem(item));
            }
        }
        return itemsDTO;
    }



    // Filtrar productos para la UI
    public ArrayList<ItemDTO> obtenerSoloProductos() {
        ArrayList<ItemDTO> itemDTOS = new ArrayList<ItemDTO>();

        for  (int i = 0; i < items.size(); i++) {
            if(items.get(i) instanceof Producto) {
                itemDTOS.add(toDTOItem(items.get(i)));
            }
        }

        return itemDTOS;
    }

    // Filtrar servicios para la UI
    public ArrayList<ItemDTO> obtenerSoloServicios() {
        ArrayList<ItemDTO> itemDTOS = new ArrayList<ItemDTO>();
        for  (int i = 0; i < items.size(); i++) {
            if(items.get(i) instanceof Servicio) {
                itemDTOS.add(toDTOItem(items.get(i)));
            }
        }
        return itemDTOS;
    }




    public UnidadDeMedidaDTO altaUnidadDeMedida(UnidadDeMedidaDTO dto) {
        UnidadDeMedida nuevaUdm = toModelUDM(dto);
        unidadesDeMedida.add(nuevaUdm);
        return toDTOUnidadDeMedida(nuevaUdm);
    }
    public void modificarUnidadDeMedida(UnidadDeMedidaDTO dto) throws Exception {
        UnidadDeMedida udm = buscarUnidadDeMedida(dto.getCodigoUnidad());
        if (udm != null) {
            udm.setDescripcionUnidad(dto.getDescripcionUnidad());
            udm.setTipoDeUnidad(TipoDeUnidad.valueOf(dto.getTipoDeUnidad()));
        } else {
            throw new Exception("Unidad de medida no encontrada");
        }
    }

    public void eliminarUnidadDeMedida(String codigo) throws Exception {
        UnidadDeMedida udm = buscarUnidadDeMedida(codigo);
        if (udm != null) {
            unidadesDeMedida.remove(udm);
        } else {
            throw new Exception("Unidad de medida no encontrada");
        }
    }

    private UnidadDeMedida buscarUnidadDeMedida(String codigo) {

        for  (int  i = 0; i < unidadesDeMedida.size(); i++) {
            if(unidadesDeMedida.get(i).getCodigoUnidad().equals(codigo)) {
                return unidadesDeMedida.get(i);
            }
        }
        return null;
    }

    public UnidadDeMedidaDTO buscarUnidadDeMedidaDTOPorDescripcion(String descripcion) {
        for (UnidadDeMedida udm : unidadesDeMedida) {
            if (udm.getDescripcionUnidad().equalsIgnoreCase(descripcion)) {
                return toDTOUnidadDeMedida(udm);
            }
        }
        return null;
    }

    public ArrayList<UnidadDeMedidaDTO> obtenerUnidadesDeMedidaDTO() {
        ArrayList<UnidadDeMedidaDTO> udmDTOs = new ArrayList<>();
        for (UnidadDeMedida udm : unidadesDeMedida) {
            udmDTOs.add(toDTOUnidadDeMedida(udm));
        }
        return udmDTOs;
    }

    public ArrayList<PrecioProveedorDTO> obtenerProveedoresPorItem(String codigoItem) {
        Item item = buscarItem(codigoItem);
        if (item == null) throw new InvalidItemException("Item no encontrado.");

        ArrayList<PrecioProveedorDTO> lista = new ArrayList<>();
        for (PrecioProveedor pp : item.getPrecioItem()) {
            lista.add(new PrecioProveedorDTO(
                    pp.getProveedor().getCuit(),
                    pp.getProveedor().getRazonSocial(),
                    item.getCodigo(),
                    item.getDescripcionDeItem(),
                    String.valueOf(pp.getPrecioItem()),
                    UtilDate.parseDate(pp.getFecha())
            ));
        }
        return lista;
    }

    public Item toModelItem(ItemDTO dto){
        UnidadDeMedida udm = buscarUnidadDeMedida(dto.getTipoUDM());
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
    private ItemDTO toDTOItem(Item item) {
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

    private UnidadDeMedida toModelUDM(UnidadDeMedidaDTO dto) {
       TipoDeUnidad tipoUnidad;

        try {
            tipoUnidad = TipoDeUnidad.valueOf(dto.getTipoDeUnidad());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new FarmaredException("Unidad de medida inexistente o inválida.");
        }
        return new  UnidadDeMedida(dto.getDescripcionUnidad(), tipoUnidad);
    }
    private UnidadDeMedidaDTO toDTOUnidadDeMedida(UnidadDeMedida udm) {
        return new UnidadDeMedidaDTO(
                udm.getCodigoUnidad(),
                udm.getDescripcionUnidad(),
                udm.getTipoDeUnidad().name()
        );
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
