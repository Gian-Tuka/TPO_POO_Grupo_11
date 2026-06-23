package Farmared.controller.item;

import Farmared.controller.proveedores.ControladorProveedores;
import Farmared.dto.item.ItemDTO;
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

        return new ItemDTO(
                item.getCodigo(),
                item.getDescripcionDeItem(),
                item.getUnidadMedida().getDescripcionUnidad(),
                item.getUnidadMedida().getCodigoUnidad(),
                item.getTipoDeIVA().name(),
                item.getRubro().getNombreRubro(),
                precioVigente
        );
    }

    public Item toModel(ItemDTO dto) throws Exception {
        Validations v = new Validations();


        UnidadDeMedida udm = buscarUnidadModelo(dto.getTipoUDM());
        if (udm == null) {
            throw new UDMException("Unidad de medida seleccionada no existe.");
        }

        TipoDeIVA enumIVA = TipoDeIVA.valueOf(dto.getTipoDeIVA());

        Rubro rubroModelo = ControladorProveedores.getInstance().buscarRubroPorId(dto.getRubro());
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

}
