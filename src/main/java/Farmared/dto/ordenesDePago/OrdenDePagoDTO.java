package Farmared.dto.ordenesDePago;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class OrdenDePagoDTO {
    private String nroOP;
    private String cuitProveedor;
    private String razonSocialProveedor;
    private String fecha;
    private ArrayList<DetalleCancelacionDTO> comprobantesCancelados;
    private float totalNetoOP;
    private ArrayList<String> retencionesEfectuadas;
    private ArrayList<FormaDePagoDTO> formasDePago;


    public OrdenDePagoDTO(String cuitProveedor, ArrayList<DetalleCancelacionDTO> comprobantesCancelados,
                          ArrayList<FormaDePagoDTO> formasDePago) {
        this.cuitProveedor = cuitProveedor;
        this.comprobantesCancelados = comprobantesCancelados;
        this.formasDePago = formasDePago;
    }


    public OrdenDePagoDTO(String nroOP, String cuitProveedor, String razonSocialProveedor, String fecha,
                          ArrayList<DetalleCancelacionDTO> comprobantesCancelados, float totalNetoOP,
                          ArrayList<String> retencionesEfectuadas, ArrayList<FormaDePagoDTO> formasDePago) {
        this.nroOP = nroOP;
        this.cuitProveedor = cuitProveedor;
        this.razonSocialProveedor = razonSocialProveedor;
        this.fecha = fecha;
        this.comprobantesCancelados = comprobantesCancelados;
        this.totalNetoOP = totalNetoOP;
        this.retencionesEfectuadas = retencionesEfectuadas;
        this.formasDePago = formasDePago;
    }

    public String getNroOP() { return nroOP; }
    public String getCuitProveedor() { return cuitProveedor; }
    public String getRazonSocialProveedor() { return razonSocialProveedor; }
    public String getFecha() { return fecha; }
    public ArrayList<DetalleCancelacionDTO> getComprobantesCancelados() { return comprobantesCancelados; }
    public float getTotalNetoOP() { return totalNetoOP; }
    public ArrayList<String> getRetencionesEfectuadas() { return retencionesEfectuadas; }
    public ArrayList<FormaDePagoDTO> getFormasDePago() { return formasDePago; }
}
