package Farmared.dto.comprobante;

public class DetalleComprobanteDTO {
    private String codigoItem;
    private String descripcionItem;
    private int cantidad;
    private float precioUnitario;
    private float subTotal;

    // Constructor para alta
    public DetalleComprobanteDTO(String codigoItem, int cantidad, float precioUnitario) {
        this.codigoItem = codigoItem;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    // Constructor para vista
    public DetalleComprobanteDTO(String codigoItem, String descripcionItem, int cantidad, float precioUnitario, float subTotal) {
        this.codigoItem = codigoItem;
        this.descripcionItem = descripcionItem;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subTotal = subTotal;
    }

    public String getCodigoItem() { return codigoItem; }
    public String getDescripcionItem() { return descripcionItem; }
    public int getCantidad() { return cantidad; }
    public float getPrecioUnitario() { return precioUnitario; }
    public float getSubTotal() { return subTotal; }

    public void setCodigoItem(String codigoItem) { this.codigoItem = codigoItem; }
    public void setDescripcionItem(String descripcionItem) { this.descripcionItem = descripcionItem; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public void setPrecioUnitario(float precioUnitario) { this.precioUnitario = precioUnitario; }
    public void setSubTotal(float subTotal) { this.subTotal = subTotal; }
}
