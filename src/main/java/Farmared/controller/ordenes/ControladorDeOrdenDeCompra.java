package Farmared.controller.ordenes;

import Farmared.controller.proveedores.ControladorProveedores;
import Farmared.controller.usuariosYSeguridad.ControladorUsuariosYSeguridad;
import Farmared.controller.item.ControladorProductosYServicios;
import Farmared.dto.ordenes.OrdenDeCompraDTO;
import Farmared.dto.ordenes.DetalleOCDTO;
import Farmared.dto.ordenes.DetalleItemDTO;
import Farmared.dto.user.UsuarioDTO;
import Farmared.dto.item.ItemDTO;
import Farmared.exception.FarmaredException;
import Farmared.exception.ProveedorNoEncontradoException;
import Farmared.exception.ItemNoEncontradoException;
import Farmared.model.cuentaCorriente.CuentaCorriente;
import Farmared.model.ordenCompra.EstadoOC;
import Farmared.model.ordenCompra.OrdenDeCompra;
import Farmared.model.ordenCompra.DetalleOC;
import Farmared.model.ordenCompra.Autorizacion;
import Farmared.model.proveedor.Proveedor;
import Farmared.model.item.Item;
import Farmared.model.precio.PrecioProveedor;
import Farmared.model.user.Usuario;
import Farmared.model.user.Rol;
import Farmared.utils.UtilDate;

import java.util.ArrayList;
import java.util.List;

public class ControladorDeOrdenDeCompra {
    private static ControladorDeOrdenDeCompra instance = null;

    private List<OrdenDeCompra> ordenesDeCompra;

    private ControladorDeOrdenDeCompra() {
        this.ordenesDeCompra = new ArrayList<>();
    }

    public synchronized static ControladorDeOrdenDeCompra getInstance() {
        if (instance == null) {
            instance = new ControladorDeOrdenDeCompra();
        }
        return instance;
    }

    // Método de mapeo privado de Modelo a DTO
    private static OrdenDeCompraDTO toDTO(OrdenDeCompra model) {
        if (model == null) return null;
        List<DetalleOCDTO> detallesDTO = new ArrayList<>();
        for (DetalleOC det : model.getDetalles()) {
            detallesDTO.add(new DetalleOCDTO(
                det.getItem().getCodigo(),
                det.getItem().getDescripcionDeItem(),
                det.getCantidad(),
                det.getPrecioUnitarioVal(),
                det.getSubtotalPorItem()
            ));
        }
        String creadorLegajo = model.obtenerCreador() != null ? model.obtenerCreador().getLegajo() : "Sistema";
        return new OrdenDeCompraDTO(
            model.getNroOC(),
            UtilDate.parseDate(model.getFechaEmision()),
            model.getProveedor().getCuit(),
            model.getProveedor().getRazonSocial(),
            model.getEstado().name(),
            model.obtenerTotalOC(),
            creadorLegajo,
            detallesDTO
        );
    }

    // Filtrar ítems del catálogo que tienen un precio definido para el proveedor elegido
    public ArrayList<ItemDTO> obtenerItemsConPrecioPorProveedor(String cuitProveedor) {
        Proveedor proveedor = ControladorProveedores.getInstance().buscarProveedorModelo(cuitProveedor);
        if (proveedor == null) {
            throw new ProveedorNoEncontradoException(cuitProveedor);
        }

        ArrayList<ItemDTO> todosLosItemsDTO = ControladorProductosYServicios.getInstance().obtenerItemsDTO();
        ArrayList<ItemDTO> itemsFiltrados = new ArrayList<>();

        for (PrecioProveedor pp : proveedor.getPrecioPorItem()) {
            String codigoItem = pp.getItem().getCodigo();
            for (ItemDTO dto : todosLosItemsDTO) {
                if (dto.getCodigo().equals(codigoItem)) {
                    itemsFiltrados.add(dto);
                    break;
                }
            }
        }
        return itemsFiltrados;
    }

    // Emisión de la Orden de Compra desde la GUI mediante DTO
    public OrdenDeCompraDTO emitirOC(OrdenDeCompraDTO dto) {
        // 1. Obtener usuario actual y buscar su modelo
        UsuarioDTO usuarioActualDTO = ControladorUsuariosYSeguridad.getInstance().getUsuarioActual();
        if (usuarioActualDTO == null) {
            throw new FarmaredException("No hay un usuario logueado en el sistema.");
        }

        Usuario usuarioActual = ControladorUsuariosYSeguridad.getInstance().buscarUsuario(usuarioActualDTO.getLegajo());
        if (usuarioActual == null) {
            throw new FarmaredException("Usuario actual no encontrado en la base de datos.");
        }

        // 2. Buscar proveedor
        Proveedor proveedor = ControladorProveedores.getInstance().buscarProveedorModelo(dto.getCuitProveedor());
        if (proveedor == null) {
            throw new ProveedorNoEncontradoException(dto.getCuitProveedor());
        }

        // 3. Crear OrdenDeCompra (nroOC y fechaEmision se generan internamente)
        OrdenDeCompra oc = new OrdenDeCompra(proveedor);
        oc.setCreador(usuarioActual);

        // 4. Agregar detalles
        if (dto.getItems() == null || dto.getItems().isEmpty()) {
            throw new FarmaredException("No se puede emitir una orden de compra vacía.");
        }

        for (DetalleItemDTO detDto : dto.getItems()) {
            Item item = ControladorProductosYServicios.getInstance().buscarItemModeloPorCodigo(detDto.getCodigoItem());
            if (item == null) {
                throw new ItemNoEncontradoException(detDto.getCodigoItem());
            }
            oc.crearDetalle(item, detDto.getCantidad());
        }

        // 5. Validar límite de deuda
        float totalOC = oc.obtenerTotalOC();
        boolean limiteOk = validarLimite(proveedor, totalOC);
        if (limiteOk) {
            oc.setEstado(EstadoOC.APROBADA);
        } else {
            oc.setEstado(EstadoOC.PENDIENTE_AUTORIZACION);
        }

        // 6. Añadir a la lista interna y retornar
        ordenesDeCompra.add(oc);
        return toDTO(oc);
    }

    public Boolean existeProveedor(Proveedor proveedor) {
        return ControladorProveedores.getInstance().buscarProveedorModelo(proveedor.getCuit()) != null;
    }

    public void añadirOC(OrdenDeCompra oc) {
        this.ordenesDeCompra.add(oc);
    }

    public Float obtenerTotalOC(OrdenDeCompra oc) {
        return oc.obtenerTotalOC();
    }

    public Boolean validarLimite(Proveedor proveedor, Float total) {
        CuentaCorriente cc = proveedor.getCuentaCorriente();
        if (cc != null) {
            float deudaActual = cc.getDeudaActual();
            float topeDeuda = cc.getTopeDeuda();
            return (deudaActual + total) <= topeDeuda;
        }
        return false;
    }

    public boolean tieneOrdenesActivas(String cuit) {
        for (OrdenDeCompra oc : ordenesDeCompra) {
            if (oc.getProveedor().getCuit().equals(cuit)
                    && (oc.getEstado() == EstadoOC.APROBADA || oc.getEstado() == EstadoOC.PENDIENTE_AUTORIZACION || oc.getEstado() == EstadoOC.APROBADA_AUTORIZACION)) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<OrdenDeCompraDTO> obtenerOrdenesDeCompraDTO() {
        ArrayList<OrdenDeCompraDTO> dtos = new ArrayList<>();
        for (OrdenDeCompra oc : ordenesDeCompra) {
            dtos.add(toDTO(oc));
        }
        return dtos;
    }

    public OrdenDeCompraDTO consultarOC(String nroOC) {
        for (OrdenDeCompra oc : ordenesDeCompra) {
            if (oc.getNroOC().equals(nroOC)) {
                return toDTO(oc);
            }
        }
        throw new FarmaredException("No se encontró la Orden de Compra: " + nroOC);
    }

    public void autorizarOC(String nroOC, String legajoSupervisor, String comentario) {
        OrdenDeCompra oc = null;
        for (OrdenDeCompra o : ordenesDeCompra) {
            if (o.getNroOC().equals(nroOC)) {
                oc = o;
                break;
            }
        }
        if (oc == null) {
            throw new FarmaredException("No se encontró la Orden de Compra: " + nroOC);
        }

        if (oc.getEstado() != EstadoOC.PENDIENTE_AUTORIZACION) {
            throw new FarmaredException("La Orden de Compra no está pendiente de autorización.");
        }

        Usuario supervisor = ControladorUsuariosYSeguridad.getInstance().buscarUsuario(legajoSupervisor);
        if (supervisor == null) {
            throw new FarmaredException("Supervisor no encontrado.");
        }

        if (supervisor.getRol() != Rol.SUPERVISOR) {
            throw new FarmaredException("El usuario autorizante debe tener rol SUPERVISOR.");
        }

        Autorizacion auth = new Autorizacion(comentario);
        auth.setSupervisor(supervisor);
        oc.setAutorizacion(auth);
        oc.setEstado(EstadoOC.APROBADA_AUTORIZACION);
    }

    public List<OrdenDeCompra> getOrdenesDeCompra() {
        return ordenesDeCompra;
    }
}
