package Farmared.dto.item;

public class PrecioProveedorDTO {
    private String cuitProveedor;
    private String razonSocial;
    private String codigoItem;
    private String descripcionItem;
    private String precio;
    private String fecha;

    public PrecioProveedorDTO(String cuitProveedor, String razonSocial, String codigoItem, String descripcionItem, String precio, String fecha) {
        this.cuitProveedor = cuitProveedor;
        this.razonSocial = razonSocial;
        this.codigoItem = codigoItem;
        this.descripcionItem = descripcionItem;
        this.precio = precio;
        this.fecha = fecha;
    }

    public String getCuitProveedor() {
        return cuitProveedor;
    }

    public void setCuitProveedor(String cuitProveedor) {
        this.cuitProveedor = cuitProveedor;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getCodigoItem() {
        return codigoItem;
    }

    public void setCodigoItem(String codigoItem) {
        this.codigoItem = codigoItem;
    }

    public String getDescripcionItem() {
        return descripcionItem;
    }

    public void setDescripcionItem(String descripcionItem) {
        this.descripcionItem = descripcionItem;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
