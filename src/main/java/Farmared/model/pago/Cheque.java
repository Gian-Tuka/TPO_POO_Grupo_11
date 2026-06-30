package Farmared.model.pago;

import Farmared.model.user.Usuario;

import java.util.Date;

public class Cheque extends FormaDePago {
    private String nroCheque;
    private TipoCheque tipo;
    private Date fechaEmision;
    private Date fechaVencimiento;
    private Usuario firmante;
    private String banco;
    private EstadoCheque estado;
    private DetalleChequeTerceros detalleTerceros;

    public Cheque(String nroCheque, TipoCheque tipo, Date fechaEmision, Date fechaVencimiento, Usuario firmante, String banco, float monto) {
        super(monto);

        this.nroCheque = nroCheque;
        this.tipo = tipo;
        this.fechaEmision = fechaEmision;
        this.fechaVencimiento = fechaVencimiento;
        this.firmante = firmante;
        this.banco = banco;
        this.estado = EstadoCheque.EN_CARTERA;
    }


    public void setDetalleTerceros(DetalleChequeTerceros detalle) {
        this.detalleTerceros = detalle;
    }
    // Getters
    public String getNroCheque() {
        return nroCheque;
    }
    public TipoCheque getTipo() { return tipo; }
    public Date getFechaEmision() {
        return fechaEmision;
    }
    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }
    public Usuario getFirmante() {
        return firmante;
    }
    public String getBanco() {
        return banco;
    }
    public EstadoCheque getEstado() { return estado; }
    public DetalleChequeTerceros getDetalleTerceros() { return detalleTerceros; }

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

    public void setFirmante(Usuario firmante) {
        this.firmante = firmante;
    }
}
