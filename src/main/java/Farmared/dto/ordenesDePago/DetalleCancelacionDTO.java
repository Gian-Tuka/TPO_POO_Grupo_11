package Farmared.dto.ordenesDePago;

public class DetalleCancelacionDTO {
    private String nroComprobante;
    private float monto;
    private String estado;


    public DetalleCancelacionDTO(String nroComprobante, float monto) {
        this.nroComprobante = nroComprobante;
        this.monto = monto;
    }

    public DetalleCancelacionDTO(String nroComprobante, float monto, String estado) {
        this.nroComprobante = nroComprobante;
        this.monto = monto;
        this.estado = estado;
    }

    public String getNroComprobante() { return nroComprobante; }
    public float getMonto() { return monto; }
    public String getEstado() { return estado; }

}
