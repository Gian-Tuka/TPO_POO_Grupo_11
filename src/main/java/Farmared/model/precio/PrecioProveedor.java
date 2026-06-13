package Farmared.model.precio;

import Farmared.model.item.Item;
import Farmared.model.proveedor.Proveedor;

import java.util.Date;

public class PrecioProveedor {
    private Item item;
    private Proveedor proveedor;
    private float precioItem;
    private Date fecha;

    public PrecioProveedor(Item item, Proveedor proveedor, float precioItem, Date fecha) {
        this.item = item;
        this.proveedor = proveedor;
        this.precioItem = precioItem;
        this.fecha = fecha;
    }

    public Item getItem() { return item; }
    public Proveedor getProveedor() { return proveedor; }
    public float getPrecioItem() { return precioItem; }
    public Date getFecha() { return fecha; }
}
