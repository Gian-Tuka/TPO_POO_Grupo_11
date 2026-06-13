package Farmared.model.item;

import Farmared.model.precio.PrecioProveedor;
import Farmared.model.rubro.Rubro;
import java.util.ArrayList;
import java.util.UUID;



public abstract class Item {
    protected String codigo;
    protected String descripcionDeItem;
    protected UnidadDeMedida cualquierCosa;
    protected UnidadDeMedida unidadMedida;
    protected ArrayList<PrecioProveedor> precioItem;
    protected TipoDeIVA tipoDeIVA;
    protected Rubro rubro;

    public Item(String descripcionDeItem, UnidadDeMedida  unidadMedida, TipoDeIVA tipoDeIVA, Rubro rubro, ArrayList<PrecioProveedor> precioItem) {
        this.codigo = generarCod();
        this.descripcionDeItem = descripcionDeItem;
        this.unidadMedida = unidadMedida;
        this.tipoDeIVA = tipoDeIVA;
        this.rubro = rubro;
        this.precioItem = new ArrayList<>();

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
        return precioItem;
    }

    public TipoDeIVA getTipoDeIVA() {
        return tipoDeIVA;
    }

    public Rubro getRubro() {
        return rubro;
    }

    private String generarCod() {
        return UUID.randomUUID().toString();
    }

    public void agregarPrecio(PrecioProveedor precio) {
        this.precioItem.add(precio);
    }
}