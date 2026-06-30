package Farmared.controller.ordenesDePago;

import Farmared.controller.impuestos.ControladorImpuestos;
import Farmared.controller.proveedores.ControladorProveedores;
import Farmared.controller.usuariosYSeguridad.ControladorUsuariosYSeguridad;
import Farmared.dto.ordenesDePago.DetalleCancelacionDTO;
import Farmared.dto.ordenesDePago.FormaDePagoDTO;
import Farmared.dto.ordenesDePago.OrdenDePagoDTO;
import Farmared.dto.user.UsuarioDTO;
import Farmared.exception.FarmaredException;
import Farmared.exception.ProveedorNoEncontradoException;
import Farmared.model.comprobante.Comprobante;
import Farmared.model.comprobante.EstadoComprobante;
import Farmared.model.impuesto.CertificadoNoRetencion;
import Farmared.model.impuesto.ImpuestoRetenible;
import Farmared.model.impuesto.RangoDeRetencion;
import Farmared.model.pago.*;
import Farmared.model.proveedor.Proveedor;
import Farmared.model.user.Usuario;
import Farmared.utils.UtilDate;

import java.util.ArrayList;
import java.util.Date;

public class ControladorDeOrdenesDePago {
    private static ControladorDeOrdenesDePago instance = null;
    private ArrayList<OrdenDePago> ordenesDePago;

    private ControladorDeOrdenesDePago() {
        this.ordenesDePago = new ArrayList<>();
    }

    public synchronized static ControladorDeOrdenesDePago getInstance() {
        if (instance == null) {
            instance = new ControladorDeOrdenesDePago();
        }
        return instance;
    }


    public OrdenDePagoDTO emitirOP(OrdenDePagoDTO dto) {

        UsuarioDTO usuarioActualDTO = ControladorUsuariosYSeguridad.getInstance().getUsuarioActual();
        if (usuarioActualDTO == null) {
            throw new FarmaredException("No hay un usuario logueado en el sistema.");
        }

        Proveedor proveedor = ControladorProveedores.getInstance().buscarProveedorModelo(dto.getCuitProveedor());
        if (proveedor == null) {
            throw new ProveedorNoEncontradoException(dto.getCuitProveedor());
        }

        // al menos un comprobante nos tienen que mandar
        if (dto.getComprobantesCancelados() == null || dto.getComprobantesCancelados().isEmpty()) {
            throw new FarmaredException("Debe seleccionar al menos un comprobante a cancelar.");
        }

        // creo la op y despues se componen del total bruto retes etc
        OrdenDePago op = toModelOrdenDePago(dto);

        float montoBruto = procesarComprobantesCancelados(op, dto.getComprobantesCancelados());


        float totalRetenido = calcularRetenciones(proveedor, montoBruto, op);

        float neto = montoBruto - totalRetenido;
        op.setTotalNetoOP(neto);

        // alguna forma de pago...
        if (dto.getFormasDePago() == null || dto.getFormasDePago().isEmpty()) {
            throw new FarmaredException("Debe especificar al menos una forma de pago.");
        }
        procesarFormasDePago(op, dto.getFormasDePago());

        // que no haya diferencias en la forma de pago y el neto
        float totalFormasPago = op.calcularTotalFormasDePago();
        if (Math.abs(totalFormasPago - neto) > 0.01f) {
            throw new FarmaredException(
                    "La suma de las formas de pago (" + totalFormasPago +
                            ") no coincide con el neto a pagar (" + neto + ").");
        }

        // aca aplicamos la cancelacion a los comprobantes porque supuestamente estaría  ok
        for (DetalleCancelacion detalle : op.getComprobantesCancelados()) {
            detalle.aplicarCancelacion();
        }
        
        // Impactamos la cuenta corriente con el nuevo saldo de los comprobantes
        proveedor.getCuentaCorriente().recalcularDeuda();

        ordenesDePago.add(op);
        return toDTO(op);
    }

    public float preCalcularRetenciones(String cuitProveedor, float montoBruto) {
        Proveedor proveedor = ControladorProveedores.getInstance().buscarProveedorModelo(cuitProveedor);
        if (proveedor == null) {
            throw new ProveedorNoEncontradoException(cuitProveedor);
        }
        float totalRetenido = 0f;
        for (ImpuestoRetenible impuesto : ControladorImpuestos.getInstance().obtenerTodosLosImpuestosModelo()) {
            if (tieneCertificadoVigente(proveedor, impuesto)) continue;
            if (montoBruto < impuesto.getMinimoNoImponible()) continue;

            for (RangoDeRetencion rango : impuesto.getRangos()) {
                if (rango.estaEnRango(montoBruto)) {
                    totalRetenido += rango.calcularRetencion(montoBruto);
                    break;
                }
            }
        }
        return totalRetenido;
    }

    private float procesarComprobantesCancelados(OrdenDePago op, ArrayList<DetalleCancelacionDTO> detallesDTO) {
        float montoBruto = 0f;

        for (DetalleCancelacionDTO detDto : detallesDTO) {
            Comprobante comp = buscarComprobantePorNro(op.getProveedor(), detDto.getNroComprobante());
            if (comp == null) {
                throw new FarmaredException("Comprobante no encontrado: " + detDto.getNroComprobante());
            }

            if (comp.getEstado() != EstadoComprobante.PENDIENTE
                    && comp.getEstado() != EstadoComprobante.AUTORIZADO
                    && comp.getEstado() != EstadoComprobante.PARCIALMENTE_PAGADO) {
                throw new FarmaredException(
                        "El comprobante " + comp.getNroComprobante() +
                                " no puede ser pagado.");
            }

            DetalleCancelacion detalle = new DetalleCancelacion(comp, detDto.getMonto());
            op.agregarComprobanteCancelado(detalle);
            montoBruto += detDto.getMonto();
        }
        return montoBruto;
    }

    private Comprobante buscarComprobantePorNro(Proveedor proveedor, String nroComprobante) {
        for (Comprobante c : proveedor.getCuentaCorriente().getComprobantes()) {
            if (c.getNroComprobante().equals(nroComprobante)) {
                return c;
            }
        }
        return null;
    }

    private float calcularRetenciones(Proveedor proveedor, float montoBruto, OrdenDePago op) {
        float totalRetenido = 0f;

        for (ImpuestoRetenible impuesto : ControladorImpuestos.getInstance().obtenerTodosLosImpuestosModelo()) {

            if (tieneCertificadoVigente(proveedor, impuesto)) {
                continue;
            }

            if (montoBruto < impuesto.getMinimoNoImponible()) {
                continue;
            }

            for (RangoDeRetencion rango : impuesto.getRangos()) {
                if (rango.estaEnRango(montoBruto)) {
                    float retencion = rango.calcularRetencion(montoBruto);
                    totalRetenido += retencion;
                    op.agregarRetencion(impuesto);
                    break;
                }
            }
        }
        return totalRetenido;
    }

    private boolean tieneCertificadoVigente(Proveedor proveedor, ImpuestoRetenible impuesto) {
        Date hoy = new Date();
        for (CertificadoNoRetencion cert : proveedor.getCertificadosNoRet()) {
            if (cert.getImpuesto().equals(impuesto) && cert.validarVigencia(hoy)) {
                return true;
            }
        }
        return false;
    }

    private void procesarFormasDePago(OrdenDePago op, ArrayList<FormaDePagoDTO> formasDTO) {
        for (FormaDePagoDTO fpDto : formasDTO) {
            FormaDePago forma = toModelFormaDePago(fpDto);
            op.agregarFormaDePago(forma);
        }
    }

    //Esto es por las vistas nada mas
    public ArrayList<DetalleCancelacionDTO> obtenerComprobantesPendientes(String cuitProveedor) {
        Proveedor proveedor = ControladorProveedores.getInstance().buscarProveedorModelo(cuitProveedor);
        if (proveedor == null) {
            throw new ProveedorNoEncontradoException(cuitProveedor);
        }

        ArrayList<DetalleCancelacionDTO> disponibles = new ArrayList<>();
        for (Comprobante c : proveedor.getCuentaCorriente().getComprobantes()) {
            if (c.getEstado() == EstadoComprobante.PENDIENTE
                    || c.getEstado() == EstadoComprobante.AUTORIZADO
                    || c.getEstado() == EstadoComprobante.PARCIALMENTE_PAGADO) {

                disponibles.add(new DetalleCancelacionDTO(
                        c.getNroComprobante(),
                        c.getSaldoPendiente(),
                        c.getEstado().name()
                ));
            }
        }
        return disponibles;
    }

    private FormaDePago toModelFormaDePago(FormaDePagoDTO dto) {
        switch (dto.getTipo()) {
            case "EFECTIVO":
                return new Efectivo(dto.getMonto());

            case "TRANSFERENCIA":
                return new Transferencia(dto.getMonto(), dto.getCbu(), dto.getBanco());

            case "CHEQUE_PROPIO": {
                Usuario firmante = ControladorUsuariosYSeguridad.getInstance()
                        .buscarUsuario(dto.getFirmanteLegajo());
                if (firmante == null) {
                    throw new FarmaredException("Firmante no encontrado: " + dto.getFirmanteLegajo());
                }
                return new Cheque(
                        dto.getNroCheque(), TipoCheque.PROPIO,
                        UtilDate.toDate(dto.getFechaEmision()),
                        UtilDate.toDate(dto.getFechaVencimiento()),
                        firmante, dto.getBanco(), dto.getMonto());
            }

            case "CHEQUE_TERCEROS": {
                Usuario firmante = ControladorUsuariosYSeguridad.getInstance()
                        .buscarUsuario(dto.getFirmanteLegajo());
                if (firmante == null) {
                    throw new FarmaredException("Firmante no encontrado: " + dto.getFirmanteLegajo());
                }
                Cheque cheque = new Cheque(
                        dto.getNroCheque(), TipoCheque.TERCERO,
                        UtilDate.toDate(dto.getFechaEmision()),
                        UtilDate.toDate(dto.getFechaVencimiento()),
                        firmante, dto.getBanco(), dto.getMonto());
                cheque.setDetalleTerceros(new DetalleChequeTerceros(dto.getCuitTercero()));
                return cheque;
            }

            default:
                throw new FarmaredException("Forma de pago no reconocida: " + dto.getTipo());
        }
    }

    private OrdenDePago toModelOrdenDePago(OrdenDePagoDTO dto) {
        Proveedor proveedor = ControladorProveedores.getInstance().buscarProveedorModelo(dto.getCuitProveedor());
        OrdenDePago op = new OrdenDePago(proveedor);

        // creo detalle de cancelacion
        for (DetalleCancelacionDTO detDto : dto.getComprobantesCancelados()) {
            Comprobante comp = buscarComprobantePorNro(proveedor, detDto.getNroComprobante());
            DetalleCancelacion detalle = new DetalleCancelacion(comp, detDto.getMonto());
            op.agregarComprobanteCancelado(detalle);
        }

        // creo forma de pago
        for (FormaDePagoDTO fpDto : dto.getFormasDePago()) {
            FormaDePago forma = toModelFormaDePago(fpDto);
            op.agregarFormaDePago(forma);
        }

        return op;
    }

    private OrdenDePagoDTO toDTO(OrdenDePago model) {
        ArrayList<DetalleCancelacionDTO> detallesDTO = new ArrayList<>();
        for (DetalleCancelacion det : model.getComprobantesCancelados()) {
            detallesDTO.add(new DetalleCancelacionDTO(
                    det.getComprobante().getNroComprobante(),
                    det.getMonto(),
                    det.getEstado().name()
            ));
        }

        ArrayList<String> retencionesDTO = new ArrayList<>();
        for (ImpuestoRetenible imp : model.getRetencionesEfectuadas()) {
            retencionesDTO.add(imp.getDescripcionRetencion());
        }

        ArrayList<FormaDePagoDTO> formasDTO = new ArrayList<>();
        for (FormaDePago fp : model.getFormaDePago()) {
            formasDTO.add(toDTOFormaDePago(fp));
        }

        return new OrdenDePagoDTO(
                model.getNroOP(),
                model.getProveedor().getCuit(),
                model.getProveedor().getRazonSocial(),
                UtilDate.parseDate(model.getFecha()),
                detallesDTO,
                model.getTotalNetoOP(),
                retencionesDTO,
                formasDTO
        );
    }

    private FormaDePagoDTO toDTOFormaDePago(FormaDePago model) {
        if (model instanceof Efectivo) {
            return new FormaDePagoDTO("EFECTIVO", model.getMonto(),
                    null, null, null, null, null, null, null);
        }
        if (model instanceof Transferencia) {
            Transferencia t = (Transferencia) model;
            return new FormaDePagoDTO("TRANSFERENCIA", model.getMonto(), t.getCbu(), t.getBancoDestino(), null, null, null, null, null);
        }
        if (model instanceof Cheque) {
            Cheque c = (Cheque) model;
            String tipo = c.getTipo() == TipoCheque.PROPIO ? "CHEQUE_PROPIO" : "CHEQUE_TERCEROS";
            String cuitTercero = c.getDetalleTerceros() != null ? c.getDetalleTerceros().getCuit() : null;
            return new FormaDePagoDTO(tipo, model.getMonto(),
                    c.getBanco(), null, c.getNroCheque(),
                    UtilDate.parseDate(c.getFechaEmision()),
                    UtilDate.parseDate(c.getFechaVencimiento()),
                    c.getFirmante().getLegajo(), cuitTercero);
        }
        throw new FarmaredException("Tipo de FormaDePago no reconocido para conversión a DTO");
    }
}
