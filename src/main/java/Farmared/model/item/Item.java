package Farmared.model.item;

import Farmared.model.precio.PrecioProveedor;
import Farmared.model.rubro.Rubro;
import Farmared.utils.GeneradorDeCodigos;
import java.util.ArrayList;
import java.util.Objects;

public abstract class Item {
    protected String codigo;
    protected String descripcionDeItem;
    protected UnidadDeMedida unidadMedida;
    protected ArrayList<PrecioProveedor> precioItem;
    protected TipoDeIVA tipoDeIVA;
    protected Rubro rubro;
    protected boolean activo;

    public Item(String descripcionDeItem, UnidadDeMedida  unidadMedida, TipoDeIVA tipoDeIVA, Rubro rubro) {
        this.codigo = generarCod();
        this.descripcionDeItem = descripcionDeItem;
        this.unidadMedida = unidadMedida;
        this.tipoDeIVA = tipoDeIVA;
        this.rubro = rubro;
        this.precioItem = new ArrayList<>();
        this.activo = true;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDescripcionDeItem() {
        return descripcionDeItem;
    }

    public UnidadDeMedida getUnidadMedida() {
        return unidadMedida;
    }

    public ArrayList<PrecioProveedor> getPrecioItem() {
        return this.precioItem;
    }

    public TipoDeIVA getTipoDeIVA() {
        return tipoDeIVA;
    }

    public Rubro getRubro() {
        return rubro;
    }

    private String generarCod() {

        GeneradorDeCodigos gdc = new GeneradorDeCodigos();

        if (this instanceof Producto) {
            this.codigo = gdc.generarCodigo("PDT", 5);
        } else if (this instanceof Servicio) {
            this.codigo = gdc.generarCodigo("SVC", 5);
        } else {
            this.codigo = gdc.generarCodigo("ITM", 5);
        }
        return codigo;
    }

    public void agregarPrecio(PrecioProveedor precio) {
        this.precioItem.add(precio);
    }

    // Sección 1.5 — Método de encapsulamiento para eliminar precios
    public void eliminarPrecio(PrecioProveedor pp) {
        precioItem.remove(pp);
    }

    // Bug 35 — Setters faltantes para atributos mutables
    public void setDescripcionDeItem(String descripcionDeItem) { this.descripcionDeItem = descripcionDeItem; }
    public void setUnidadMedida(UnidadDeMedida unidadMedida) { this.unidadMedida = unidadMedida; }
    public void setTipoDeIVA(TipoDeIVA tipoDeIVA) { this.tipoDeIVA = tipoDeIVA; }
    public void setRubro(Rubro rubro) { this.rubro = rubro; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    // Bug 12 — equals/hashCode basado en codigo (clave natural única)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Item)) return false;
        Item item = (Item) o;
        return Objects.equals(codigo, item.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }
}