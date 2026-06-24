package Farmared.model.ordenCompra;

import Farmared.model.item.Item;
import Farmared.model.precio.PrecioProveedor;
import Farmared.model.proveedor.Proveedor;
import Farmared.exception.FarmaredException;

public class DetalleOC {
    private Item item;
    private int cantidad;
    private PrecioProveedor precioUnitario;
    private Float subtotalPorItem;

    public DetalleOC(Item item, int cantidad) {
        this.item = item;
        this.cantidad = cantidad;
        this.subtotalPorItem = 0f;
    }

    public Float calcularSubtotal() {
        if (precioUnitario != null) {
            this.subtotalPorItem = cantidad * precioUnitario.getPrecioItem();
            return this.subtotalPorItem;
        }
        return 0f;
    }

    public void obtenerPrecioProveedor(Proveedor proveedor) {
        for (PrecioProveedor pp : proveedor.getPrecioPorItem()) {
            if (pp.getItem().equals(this.item)) {
                this.precioUnitario = pp;
                this.calcularSubtotal();
                return;
            }
        }
        throw new FarmaredException("No se encontró el precio para el item: " + 
                (item != null ? item.getDescripcionDeItem() : "null") + " en el proveedor: " + proveedor.getRazonSocial());
    }

    public Item getItem() {
        return item;
    }

    public int getCantidad() {
        return cantidad;
    }

    public Float getSubtotalPorItem() {
        return subtotalPorItem != null ? subtotalPorItem : 0f;
    }

    public float getPrecioUnitarioVal() {
        return precioUnitario != null ? precioUnitario.getPrecioItem() : 0f;
    }
}
