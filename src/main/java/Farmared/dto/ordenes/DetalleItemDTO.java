package Farmared.dto.ordenes;

public class DetalleItemDTO {
    private String codigoItem;
    private int cantidad;

    public DetalleItemDTO(String codigoItem, int cantidad) {
        this.codigoItem = codigoItem;
        this.cantidad = cantidad;
    }

    public String getCodigoItem() {
        return codigoItem;
    }

    public void setCodigoItem(String codigoItem) {
        this.codigoItem = codigoItem;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
