package Farmared.controller.comprobantes;

import Farmared.controller.item.ControladorProductosYServicios;
import Farmared.controller.ordenes.ControladorDeOrdenDeCompra;
import Farmared.controller.proveedores.ControladorProveedores;
import Farmared.dto.comprobante.*;
import Farmared.dto.item.ItemDTO;
import Farmared.dto.proveedor.ProveedorDTO;
import Farmared.exception.ComprobanteNoEncontradoException;
import Farmared.exception.FarmaredException;
import Farmared.model.comprobante.*;
import Farmared.model.item.Item;
import Farmared.model.ordenCompra.OrdenDeCompra;
import Farmared.model.proveedor.Proveedor;

import java.util.ArrayList;
import java.util.Date;

public class ControladorComprobantes {
    private static ControladorComprobantes instance = null;

    private ArrayList<Factura> facturas;
    private ArrayList<NotaCredito> notasDeCredito;
    private ArrayList<NotaDebito> notasDeDebito;
    private int contadorNroComprobante;

    private ControladorComprobantes() {
        this.facturas = new ArrayList<>();
        this.notasDeCredito = new ArrayList<>();
        this.notasDeDebito = new ArrayList<>();
        this.contadorNroComprobante = 1;
    }

    public synchronized static ControladorComprobantes getInstance() {
        if (instance == null) {
            instance = new ControladorComprobantes();
        }
        return instance;
    }

    // --- FACTURAS ---
    public FacturaDTO altaFactura(FacturaDTO dto) {
        Proveedor proveedor = ControladorProveedores.getInstance().buscarProveedorModelo(dto.getCuitProveedor());
        if (proveedor == null) {
            throw new FarmaredException("Proveedor no encontrado: " + dto.getCuitProveedor());
        }

        OrdenDeCompra oc = buscarOrdenDeCompra(dto.getNroOC());
        TipoFactura tipoFactura = TipoFactura.valueOf(dto.getTipoFactura());
        Date fecha = new Date();
        
        Factura factura = new Factura(contadorNroComprobante++, fecha, proveedor, dto.getDescripcion(), oc, tipoFactura);

        if (dto.getDetalles() != null) {
            for (DetalleComprobanteDTO detDTO : dto.getDetalles()) {
                Item item = ControladorProductosYServicios.getInstance().buscarItemModeloPorCodigo(detDTO.getCodigoItem());
                if (item != null) {
                    DetalleComprobante det = new DetalleComprobante(item, detDTO.getCantidad(), detDTO.getPrecioUnitario());
                    factura.agregarDetalle(det);
                }
            }
        }

        facturas.add(factura);
        proveedor.getCuentaCorriente().agregarComprobante(factura);

        return toDTO(factura);
    }

    public void modificarFactura(FacturaDTO dto) {
        Factura factura = buscarFactura(dto.getNroComprobante());
        if (factura == null) {
            throw new ComprobanteNoEncontradoException(dto.getNroComprobante());
        }
        
        factura.setDescripcion(dto.getDescripcion());
        factura.setEstado(EstadoComprobante.valueOf(dto.getEstado()));
    }

    public FacturaDTO consultarFactura(int nroComprobante) {
        Factura f = buscarFactura(nroComprobante);
        if (f == null) {
            throw new ComprobanteNoEncontradoException(nroComprobante);
        }
        return toDTO(f);
    }

    public ArrayList<FacturaDTO> obtenerFacturasDTO() {
        ArrayList<FacturaDTO> list = new ArrayList<>();
        for (Factura f : facturas) {
            list.add(toDTO(f));
        }
        return list;
    }

    // --- NOTAS DE CREDITO ---
    public NotaCreditoDTO altaNotaDeCredito(NotaCreditoDTO dto) {
        Proveedor proveedor = ControladorProveedores.getInstance().buscarProveedorModelo(dto.getCuitProveedor());
        if (proveedor == null) {
            throw new FarmaredException("Proveedor no encontrado: " + dto.getCuitProveedor());
        }

        Factura facturaAsociada = buscarFactura(dto.getNroFacturaAsociada());
        
        NotaCredito nc = new NotaCredito(contadorNroComprobante++, new Date(), dto.getMonto(), proveedor, dto.getDescripcion(), facturaAsociada);
        
        // Si el monto de la Nota de Credito supera el tope de deuda del proveedor, requiere autorización.
        if (dto.getMonto() > proveedor.getCuentaCorriente().getTopeDeuda()) {
            nc.setEstado(EstadoComprobante.PENDIENTE_AUTORIZACION);
            // No se agrega a la cuenta corriente hasta ser autorizada
        } else {
            nc.setEstado(EstadoComprobante.PENDIENTE);
            proveedor.getCuentaCorriente().agregarComprobante(nc);
        }
        
        notasDeCredito.add(nc);
        return toDTO(nc);
    }

    public void modificarNotaDeCredito(NotaCreditoDTO dto) {
        NotaCredito nc = buscarNotaDeCredito(dto.getNroComprobante());
        if (nc == null) {
            throw new ComprobanteNoEncontradoException(dto.getNroComprobante());
        }
        nc.setDescripcion(dto.getDescripcion());
        nc.setEstado(EstadoComprobante.valueOf(dto.getEstado()));
    }

    public void autorizarNotaDeCredito(int nroComprobante) {
        NotaCredito nc = buscarNotaDeCredito(nroComprobante);
        if (nc != null && nc.getEstado() == EstadoComprobante.PENDIENTE_AUTORIZACION) {
            nc.setEstado(EstadoComprobante.AUTORIZADO);
            nc.getProveedor().getCuentaCorriente().agregarComprobante(nc);
        }
    }

    public ArrayList<NotaCreditoDTO> obtenerNotasDeCreditoDTO() {
        ArrayList<NotaCreditoDTO> list = new ArrayList<>();
        for (NotaCredito nc : notasDeCredito) {
            list.add(toDTO(nc));
        }
        return list;
    }

    // --- NOTAS DE DEBITO ---
    public NotaDebitoDTO altaNotaDeDebito(NotaDebitoDTO dto) {
        Proveedor proveedor = ControladorProveedores.getInstance().buscarProveedorModelo(dto.getCuitProveedor());
        if (proveedor == null) {
            throw new FarmaredException("Proveedor no encontrado: " + dto.getCuitProveedor());
        }
        
        NotaDebito nd = new NotaDebito(contadorNroComprobante++, new Date(), dto.getMonto(), proveedor, dto.getDescripcion());
        
        proveedor.getCuentaCorriente().agregarComprobante(nd);
        notasDeDebito.add(nd);
        
        return toDTO(nd);
    }

    public void modificarNotaDeDebito(NotaDebitoDTO dto) {
        NotaDebito nd = buscarNotaDeDebito(dto.getNroComprobante());
        if (nd == null) {
            throw new ComprobanteNoEncontradoException(dto.getNroComprobante());
        }
        nd.setDescripcion(dto.getDescripcion());
        nd.setEstado(EstadoComprobante.valueOf(dto.getEstado()));
    }

    public ArrayList<NotaDebitoDTO> obtenerNotasDeDebitoDTO() {
        ArrayList<NotaDebitoDTO> list = new ArrayList<>();
        for (NotaDebito nd : notasDeDebito) {
            list.add(toDTO(nd));
        }
        return list;
    }

    // --- COMBO HELPERS FOR VISTAS ---
    public ArrayList<ProveedorDTO> obtenerProveedoresParaCombo() {
        return ControladorProveedores.getInstance().obtenerProveedoresDTO();
    }

    public ArrayList<ItemDTO> obtenerItemsParaCombo() {
        ArrayList<ItemDTO> items = new ArrayList<>();
        items.addAll(ControladorProductosYServicios.getInstance().obtenerSoloProductos());
        items.addAll(ControladorProductosYServicios.getInstance().obtenerSoloServicios());
        return items;
    }

    // --- INTERNAL METHODS ---
    private Factura buscarFactura(int nro) {
        for (Factura f : facturas) {
            if (f.getNroComprobante() == nro) return f;
        }
        return null;
    }

    private NotaCredito buscarNotaDeCredito(int nro) {
        for (NotaCredito nc : notasDeCredito) {
            if (nc.getNroComprobante() == nro) return nc;
        }
        return null;
    }

    private NotaDebito buscarNotaDeDebito(int nro) {
        for (NotaDebito nd : notasDeDebito) {
            if (nd.getNroComprobante() == nro) return nd;
        }
        return null;
    }

    private OrdenDeCompra buscarOrdenDeCompra(String nroOC) {
        if (nroOC == null || nroOC.isEmpty()) return null;
        for (OrdenDeCompra oc : ControladorDeOrdenDeCompra.getInstance().getOrdenesDeCompra()) {
            if (oc.getNroOC().equals(nroOC)) return oc;
        }
        return null;
    }

    // --- TO DTO METHODS ---
    private FacturaDTO toDTO(Factura f) {
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

    private NotaCreditoDTO toDTO(NotaCredito nc) {
        int nroFac = nc.getFacturaAsociada() != null ? nc.getFacturaAsociada().getNroComprobante() : 0;
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
