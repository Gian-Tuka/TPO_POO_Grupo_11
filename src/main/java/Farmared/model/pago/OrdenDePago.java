package Farmared.model.pago;

import Farmared.model.impuesto.ImpuestoRetenible;
import Farmared.model.proveedor.Proveedor;
import Farmared.utils.GeneradorDeCodigos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class OrdenDePago {
    private String nroOP;
    private Proveedor proveedor;
    private Date fecha;
    private ArrayList<DetalleCancelacion> comprobantesCancelados;
    private float totalNetoOP;
    private ArrayList<ImpuestoRetenible> retencionesEfectuadas;
    private ArrayList<FormaDePago> formasDePago;

    public OrdenDePago(Proveedor proveedor) {
        this.proveedor = proveedor;

        this.nroOP = generarNroOP();
        this.fecha = generarFecha();
        this.comprobantesCancelados = new ArrayList<>();
        this.retencionesEfectuadas = new ArrayList<>();
        this.formasDePago = new ArrayList<>();
        this.totalNetoOP = 0f;
}
    private String generarNroOP() {
        GeneradorDeCodigos gdc = new GeneradorDeCodigos();
        return gdc.generarCodigo("OP", 4);
    }
    private Date generarFecha() {
        return new Date();
    }


    public void agregarComprobanteCancelado(DetalleCancelacion detalle) {
        this.comprobantesCancelados.add(detalle);
    }

    public void agregarRetencion(ImpuestoRetenible impuesto) {
        this.retencionesEfectuadas.add(impuesto);
    }

    public void agregarFormaDePago(FormaDePago forma) {
        this.formasDePago.add(forma);
    }

    public void setTotalNetoOP(float total) {
        this.totalNetoOP = total;
    }

    public float calcularTotalFormasDePago() {
        float total = 0f;
        for (FormaDePago fp : formasDePago) {
            total += fp.getMonto();
        }
        return total;
    }


    public String getNroOP() { return nroOP; }
    public Proveedor getProveedor() { return proveedor; }
    public Date getFecha() { return fecha; }
    public List<DetalleCancelacion> getComprobantesCancelados() { return comprobantesCancelados; }
    public float getTotalNetoOP() { return totalNetoOP; }
    public List<ImpuestoRetenible> getRetencionesEfectuadas() { return retencionesEfectuadas; }
    public List<FormaDePago> getFormaDePago() { return formasDePago; }
}
