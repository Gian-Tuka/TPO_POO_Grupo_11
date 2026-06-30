package Farmared.controller.comprobantes;

import Farmared.controller.item.ControladorProductosYServicios;
import Farmared.controller.ordenes.ControladorDeOrdenDeCompra;
import Farmared.controller.proveedores.ControladorProveedores;
import Farmared.dto.comprobante.*;
import Farmared.exception.FarmaredException;
import Farmared.model.comprobante.*;
import Farmared.model.item.Item;
import Farmared.model.ordenCompra.EstadoOC;
import Farmared.model.ordenCompra.OrdenDeCompra;
import Farmared.model.proveedor.Proveedor;

import java.util.ArrayList;

public class ControladorComprobantes {
    private static ControladorComprobantes instance = null;

    private ArrayList<Comprobante> comprobantes;

    private ControladorComprobantes() {
        this.comprobantes = new ArrayList<>();
    }

    public synchronized static ControladorComprobantes getInstance() {
        if (instance == null) {
            instance = new ControladorComprobantes();
        }
        return instance;
    }

    // =====================================================================
    // REGISTRO DE FACTURAS
    // Reglas:
    //   - Sin OC -> PENDIENTE_AUTORIZACION, NO afecta CC
    //   - Con OC y con desvíos -> PENDIENTE_AUTORIZACION, NO afecta CC
    //   - Con OC y sin desvíos -> PENDIENTE, afecta CC (incrementa deuda)
    // =====================================================================
    public FacturaDTO registrarFactura(FacturaDTO dto) throws Exception {
        Proveedor proveedor = ControladorProveedores.getInstance().buscarProveedorModelo(dto.getCuitProveedor());
        if (proveedor == null) {
            throw new FarmaredException("Error: El Proveedor con CUIT " + dto.getCuitProveedor() + " no existe");
        }

        OrdenDeCompra oc = null;
        if (dto.getNroOC() != null && !dto.getNroOC().trim().isEmpty()) {
            oc = ControladorDeOrdenDeCompra.getInstance().buscarOrdenDeCompraModelo(dto.getNroOC());
            if (oc == null) {
                throw new FarmaredException("La orden de compra " + dto.getNroOC() + " no existe.");
            }
            if (!oc.getProveedor().getCuit().equals(proveedor.getCuit())) {
                throw new FarmaredException("La orden de compra " + dto.getNroOC() + " no pertenece al proveedor seleccionado.");
            }
        }

        Factura factura = toModelFactura(dto);

        if (dto.getDetalles() != null) {
            for (DetalleComprobanteDTO detDTO : dto.getDetalles()) {
                Item item = ControladorProductosYServicios.getInstance().buscarItem(detDTO.getCodigoItem());
                if (item == null) {
                    throw new FarmaredException("Error: El ítem " + detDTO.getCodigoItem() + " de la factura no existe.");
                }
                DetalleComprobante detComp = new DetalleComprobante(item, detDTO.getCantidad(), detDTO.getPrecioUnitario());
                factura.agregarDetalle(detComp);
            }
        }

        factura.calcularTotalesYSubtotales();

        // VALIDACIÓN: Exceso de tope de deuda
        boolean excedeTope = false;
        if (proveedor.getCuentaCorriente() != null) {
            float deudaSimulada = proveedor.getCuentaCorriente().getDeudaActual() + factura.getTotal();
            if (deudaSimulada > proveedor.getCuentaCorriente().getTopeDeuda()) {
                excedeTope = true;
            }
        }

        if (oc == null) {
            // Sin OC -> compra directa, requiere autorización supervisora
            factura.setEstado(EstadoComprobante.PENDIENTE_AUTORIZACION);
        } else if (factura.tieneDesvios()) {
            // Con OC pero con desvíos en ítems/precios -> requiere autorización
            factura.setEstado(EstadoComprobante.PENDIENTE_AUTORIZACION);
        } else if (excedeTope) {
            // Excede el tope de la cuenta corriente -> requiere autorización
            factura.setEstado(EstadoComprobante.PENDIENTE_AUTORIZACION);
        } else {
            // Con OC y sin desvíos ni excesos -> PENDIENTE
            factura.setEstado(EstadoComprobante.PENDIENTE);
            oc.setEstado(EstadoOC.CERRADA);
        }

        this.comprobantes.add(factura);
        if (proveedor.getCuentaCorriente() != null) {
            proveedor.getCuentaCorriente().agregarComprobante(factura);
            proveedor.getCuentaCorriente().recalcularDeuda();
        }

        return toDTOFactura(factura);
    }

    // =====================================================================
    // REGISTRO DE NOTA DE CRÉDITO
    // =====================================================================
    public NotaCreditoDTO registrarNotaCredito(NotaCreditoDTO dto) throws Exception {
        Proveedor proveedor = ControladorProveedores.getInstance().buscarProveedorModelo(dto.getCuitProveedor());
        if (proveedor == null) {
            throw new FarmaredException("Error: Proveedor no encontrado para la Nota de Crédito.");
        }

        Factura facturaAsociada = null;
        if (dto.getNroFacturaAsociada() != null && !dto.getNroFacturaAsociada().trim().isEmpty()) {
            Comprobante comp = buscarComprobanteModelo(dto.getNroFacturaAsociada());
            if (comp instanceof Factura) {
                facturaAsociada = (Factura) comp;
                if (!facturaAsociada.getProveedor().getCuit().equals(proveedor.getCuit())) {
                    throw new FarmaredException("Error: La Factura asociada no pertenece al proveedor de la NC.");
                }
            } else {
                throw new FarmaredException("Error: El comprobante " + dto.getNroFacturaAsociada() + " no es una Factura válida.");
            }
        }

        NotaCredito nc = new NotaCredito(proveedor, dto.getDescripcion(), facturaAsociada, dto.getMonto());

        if (facturaAsociada != null) {
            nc.setEstado(EstadoComprobante.PENDIENTE);
        } else {
            nc.setEstado(EstadoComprobante.PENDIENTE_AUTORIZACION);
        }

        this.comprobantes.add(nc);
        if (proveedor.getCuentaCorriente() != null) {
            proveedor.getCuentaCorriente().agregarComprobante(nc);
            proveedor.getCuentaCorriente().recalcularDeuda();
        }

        return toDTO(nc);
    }

    // =====================================================================
    // REGISTRO DE NOTA DE DÉBITO
    // =====================================================================
    public NotaDebitoDTO registrarNotaDebito(NotaDebitoDTO dto) throws Exception {
        Proveedor proveedor = ControladorProveedores.getInstance().buscarProveedorModelo(dto.getCuitProveedor());
        if (proveedor == null) {
            throw new FarmaredException("Error: Proveedor no encontrado para la Nota de Débito.");
        }

        Factura facturaAsociada = null;
        if (dto.getNroFacturaAsociada() != null && !dto.getNroFacturaAsociada().trim().isEmpty()) {
            Comprobante comp = buscarComprobanteModelo(dto.getNroFacturaAsociada());
            if (comp instanceof Factura) {
                facturaAsociada = (Factura) comp;
                if (!facturaAsociada.getProveedor().getCuit().equals(proveedor.getCuit())) {
                    throw new FarmaredException("Error: La Factura asociada no pertenece al proveedor de la ND.");
                }
            } else {
                throw new FarmaredException("Error: El comprobante " + dto.getNroFacturaAsociada() + " no es una Factura válida.");
            }
        }

        NotaDebito nd = new NotaDebito(proveedor, dto.getDescripcion(), dto.getMonto());

        boolean excedeTope = false;
        if (proveedor.getCuentaCorriente() != null) {
            float deudaSimulada = proveedor.getCuentaCorriente().getDeudaActual() + dto.getMonto();
            if (deudaSimulada > proveedor.getCuentaCorriente().getTopeDeuda()) {
                excedeTope = true;
            }
        }

        if (facturaAsociada != null && !excedeTope) {
            nd.setEstado(EstadoComprobante.PENDIENTE);
        } else {
            nd.setEstado(EstadoComprobante.PENDIENTE_AUTORIZACION);
        }

        this.comprobantes.add(nd);
        if (proveedor.getCuentaCorriente() != null) {
            proveedor.getCuentaCorriente().agregarComprobante(nd);
            proveedor.getCuentaCorriente().recalcularDeuda();
        }

        return toDTO(nd);
    }

    // =====================================================================
    // AUTORIZACIÓN (solo aplica a PENDIENTE_AUTORIZACION)
    // =====================================================================
    public void autorizarComprobante(String codigoComprobante) throws Exception {
        Comprobante comp = buscarComprobanteModelo(codigoComprobante);
        if (comp == null) {
            throw new FarmaredException("Error: El comprobante " + codigoComprobante + " no existe.");
        }
        if (comp.getEstado() != EstadoComprobante.PENDIENTE_AUTORIZACION) {
            throw new FarmaredException("El comprobante " + codigoComprobante +
                " no está pendiente de autorización. Estado actual: " + comp.getEstado().name() + ".");
        }

        comp.setEstado(EstadoComprobante.AUTORIZADO);

        if (comp instanceof Factura) {
            Factura f = (Factura) comp;
            if (f.getOrdenDeCompra() != null) {
                f.getOrdenDeCompra().setEstado(EstadoOC.CERRADA);
            }
        }

        Proveedor proveedor = comp.getProveedor();
        if (proveedor.getCuentaCorriente() != null) {
            // Ya está agregado, solo hace falta recalcular porque cambió de estado
            proveedor.getCuentaCorriente().recalcularDeuda();
        }
    }

    // =====================================================================
    // BÚSQUEDA Y CONSULTA
    // =====================================================================
    public Comprobante buscarComprobanteModelo(String codigo) {
        if (codigo == null || codigo.isEmpty()) return null;
        for (Comprobante c : comprobantes) {
            if (c.getNroComprobante().equals(codigo)) return c;
        }
        return null;
    }

    public void modificarFactura(FacturaDTO dto) throws Exception {
        Comprobante comp = buscarComprobanteModelo(dto.getNroComprobante());
        if (comp instanceof Factura) {
            ((Factura) comp).setDescripcion(dto.getDescripcion());
        } else {
            throw new FarmaredException("Error: La factura con código " + dto.getNroComprobante() + " no existe.");
        }
    }

    public void modificarNotaDeDebito(NotaDebitoDTO dto) throws Exception {
        Comprobante comp = buscarComprobanteModelo(dto.getNroComprobante());
        if (comp instanceof NotaDebito) {
            ((NotaDebito) comp).setDescripcion(dto.getDescripcion());
        } else {
            throw new FarmaredException("Error: La Nota de Débito con código " + dto.getNroComprobante() + " no existe.");
        }
    }

    public ArrayList<FacturaDTO> obtenerFacturasDTO() {
        ArrayList<FacturaDTO> listaDTO = new ArrayList<>();
        for (Comprobante c : this.comprobantes) {
            if (c instanceof Factura) listaDTO.add(toDTOFactura((Factura) c));
        }
        return listaDTO;
    }

    public ArrayList<NotaDebitoDTO> obtenerNotasDeDebitoDTO() {
        ArrayList<NotaDebitoDTO> listaDTO = new ArrayList<>();
        for (Comprobante c : this.comprobantes) {
            if (c instanceof NotaDebito) listaDTO.add(toDTO((NotaDebito) c));
        }
        return listaDTO;
    }

    public ArrayList<NotaCreditoDTO> obtenerNotasDeCreditoDTO() {
        ArrayList<NotaCreditoDTO> listaDTO = new ArrayList<>();
        for (Comprobante c : this.comprobantes) {
            if (c instanceof NotaCredito) listaDTO.add(toDTO((NotaCredito) c));
        }
        return listaDTO;
    }

    public FacturaDTO consultarFactura(String codigo) {
        for (Comprobante comp : comprobantes) {
            if (comp instanceof Factura && comp.getNroComprobante().equals(codigo)) {
                return toDTOFactura((Factura) comp);
            }
        }
        throw new FarmaredException("No se encontró la factura con código: " + codigo);
    }

    // =====================================================================
    // CONVERSIONES MODEL <-> DTO
    // =====================================================================
    private FacturaDTO toDTOFactura(Factura f) {
        String nroOC = f.getOrdenDeCompra() != null ? f.getOrdenDeCompra().getNroOC() : "";
        ArrayList<DetalleComprobanteDTO> detDTOs = new ArrayList<>();
        for (DetalleComprobante d : f.getDetalles()) {
            detDTOs.add(toDTO(d));
        }
        return new FacturaDTO(
            f.getNroComprobante(),
            Farmared.utils.UtilDate.parseDate(f.getFecha()),
            f.getProveedor().getCuit(),
            f.getProveedor().getRazonSocial(),
            f.getDescripcion(),
            f.getTotal(),
            f.getEstado().name(),
            nroOC,
            detDTOs,
            f.getTipoFactura().name()
        );
    }

    private Factura toModelFactura(FacturaDTO dto) throws Exception {
        Proveedor proveedor = ControladorProveedores.getInstance().buscarProveedorModelo(dto.getCuitProveedor());
        OrdenDeCompra oc = ControladorDeOrdenDeCompra.getInstance().buscarOrdenDeCompraModelo(dto.getNroOC());
        TipoFactura tipoFactura = TipoFactura.valueOf(dto.getTipoFactura());
        return new Factura(proveedor, dto.getDescripcion(), oc, tipoFactura);
    }

    private NotaCreditoDTO toDTO(NotaCredito nc) {
        String nroFac = nc.getFacturaAsociada() != null ? nc.getFacturaAsociada().getNroComprobante() : "";
        boolean requiere = nc.getEstado() == EstadoComprobante.PENDIENTE_AUTORIZACION;
        return new NotaCreditoDTO(
            nc.getNroComprobante(),
            Farmared.utils.UtilDate.parseDate(nc.getFecha()),
            nc.getProveedor().getCuit(),
            nc.getProveedor().getRazonSocial(),
            nc.getDescripcion(),
            nc.getTotal(),
            nc.getEstado().name(),
            nroFac,
            requiere
        );
    }

    private NotaDebitoDTO toDTO(NotaDebito nd) {
        return new NotaDebitoDTO(
            nd.getNroComprobante(),
            Farmared.utils.UtilDate.parseDate(nd.getFecha()),
            nd.getProveedor().getCuit(),
            nd.getProveedor().getRazonSocial(),
            nd.getDescripcion(),
            nd.getTotal(),
            nd.getEstado().name()
        );
    }

    private DetalleComprobanteDTO toDTO(DetalleComprobante d) {
        return new DetalleComprobanteDTO(
            d.getItem().getCodigo(),
            d.getItem().getDescripcionDeItem(),
            d.getCantidad(),
            d.getPrecioFacturado(),
            d.getSubTotal()
        );
    }
}
