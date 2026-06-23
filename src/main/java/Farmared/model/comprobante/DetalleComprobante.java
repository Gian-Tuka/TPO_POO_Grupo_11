package Farmared.model.comprobante;

import Farmared.model.item.Item;

public class DetalleComprobante {
    private Item item;
    private int cantidad;
    private float precioFacturado;
    private float subTotal;

    public DetalleComprobante(Item item, int cantidad, float precioFacturado) {
        this.item = item;
        this.cantidad = cantidad;
        this.precioFacturado = precioFacturado;
        this.subTotal = calcularSubTotal();
    }

    public float calcularSubTotal() {
        return cantidad * precioFacturado;
    }

    // Getters
    public Item getItem() {
        return item;
    }

    public int getCantidad() {
        return cantidad;
    }

    public float getPrecioFacturado() {
        return precioFacturado;
    }

    public float getSubTotal() {
        return subTotal;
    }

    // Setters
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        this.subTotal = calcularSubTotal();
    }

    public void setPrecioFacturado(float precioFacturado) {
        this.precioFacturado = precioFacturado;
        this.subTotal = calcularSubTotal();
    }
}
