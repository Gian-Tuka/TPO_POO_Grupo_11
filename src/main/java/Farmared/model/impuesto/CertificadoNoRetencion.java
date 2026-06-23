package Farmared.model.impuesto;

import Farmared.model.proveedor.Proveedor;

import java.util.Date;

public class CertificadoNoRetencion {
    private ImpuestoRetenible impuesto;
    private Date fechaInicio;
    private Date fechaVencimiento;
    private Proveedor proveedor;


    public CertificadoNoRetencion(ImpuestoRetenible impuesto, Date inicio, Date vencimiento, Proveedor proveedor ) {
        this.impuesto = impuesto;
        this.fechaInicio = inicio;
        this.fechaVencimiento = vencimiento;
        this.proveedor = proveedor;
    }

    // Sección 1.4 — Bug 10: Lógica real implementada (antes retornaba null)
    public boolean validarVigencia(Date fechaHoy) {
        return !fechaHoy.before(fechaInicio) && !fechaHoy.after(fechaVencimiento);
    }

    // Getters faltantes (sección 1.3)
    public ImpuestoRetenible getImpuesto() { return impuesto; }
    public Date getFechaInicio() { return fechaInicio; }
    public Date getFechaVencimiento() { return fechaVencimiento; }
    public Proveedor getProveedor() { return proveedor; }
}