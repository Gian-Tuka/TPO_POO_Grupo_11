package Farmared.dto.ordenesDePago;

public class FormaDePagoDTO {
    private String tipo;
    private float monto;
    private String banco;
    private String cbu;
    private String nroCheque;
    private String fechaEmision;
    private String fechaVencimiento;
    private String firmanteLegajo;
    private String cuitTercero;

    public FormaDePagoDTO(String tipo, float monto, String banco, String cbu,
                          String nroCheque, String fechaEmision, String fechaVencimiento,
                          String firmanteLegajo, String cuitTercero) {
        this.tipo = tipo;
        this.monto = monto;
        this.banco = banco;
        this.cbu = cbu;
        this.nroCheque = nroCheque;
        this.fechaEmision = fechaEmision;
        this.fechaVencimiento = fechaVencimiento;
        this.firmanteLegajo = firmanteLegajo;
        this.cuitTercero = cuitTercero;
    }

    public String getTipo() { return tipo; }
    public float getMonto() { return monto; }
    public String getBanco() { return banco; }
    public String getCbu() { return cbu; }
    public String getNroCheque() { return nroCheque; }
    public String getFechaEmision() { return fechaEmision; }
    public String getFechaVencimiento() { return fechaVencimiento; }
    public String getFirmanteLegajo() { return firmanteLegajo; }
    public String getCuitTercero() { return cuitTercero; }
}
