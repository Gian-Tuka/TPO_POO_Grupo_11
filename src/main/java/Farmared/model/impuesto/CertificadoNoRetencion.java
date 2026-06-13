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

    private Boolean validarVigencia(Date fechaHoy){
        return null;
    }
}