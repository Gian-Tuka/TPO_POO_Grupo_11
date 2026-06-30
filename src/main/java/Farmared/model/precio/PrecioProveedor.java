package Farmared.model.precio;

import Farmared.model.item.Item;
import Farmared.model.proveedor.Proveedor;

import java.util.Date;

public class PrecioProveedor {
    private Item item;
    private Proveedor proveedor;
    private float precioItem;
    private Date fecha;

    public PrecioProveedor(Item item, Proveedor proveedor, float precioItem) {
        this.item = item;
        this.proveedor = proveedor;
        this.precioItem = precioItem;
        this.fecha = obtenerFecha();
    }

    public Item getItem() { return item; }
    public Proveedor getProveedor() { return proveedor; }
    public float getPrecioItem() { return precioItem; }
    public Date getFecha() { return fecha; }
    private Date obtenerFecha() {
        return new Date();
    }

    // Setter faltante para precio (sección 1.1) — ítem y proveedor son inmutables por diseño
    public void setPrecio(float precio) {
        if (precio < 0) throw new IllegalArgumentException("El precio no puede ser negativo");
        this.precioItem = precio;
    }
}
