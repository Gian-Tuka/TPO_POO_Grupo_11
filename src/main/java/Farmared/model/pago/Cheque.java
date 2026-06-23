package Farmared.model.pago;

import java.util.Date;

public class Cheque extends FormaDePago {
    private String nroCheque;
    private Date fechaEmision;
    private Date fechaVencimiento;
    private String banco;
    private String firmante;

    public Cheque(float monto, Date fecha, String nroCheque, Date fechaEmision,
                  Date fechaVencimiento, String banco, String firmante) {
        super(monto, fecha);
        this.nroCheque = nroCheque;
        this.fechaEmision = fechaEmision;
        this.fechaVencimiento = fechaVencimiento;
        this.banco = banco;
        this.firmante = firmante;
    }

    // Getters
    public String getNroCheque() {
        return nroCheque;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public String getBanco() {
        return banco;
    }

    public String getFirmante() {
        return firmante;
    }

    // Setters
    public void setNroCheque(String nroCheque) {
        this.nroCheque = nroCheque;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public void setFirmante(String firmante) {
        this.firmante = firmante;
    }
}
