package Farmared.model.item;

import Farmared.model.precio.PrecioProveedor;
import Farmared.model.rubro.Rubro;
import Farmared.utils.GeneradorDeCodigos;
import java.util.ArrayList;
import java.util.UUID;

public abstract class Item {
    protected String codigo;
    protected String descripcionDeItem;
    protected UnidadDeMedida unidadMedida;
    protected ArrayList<PrecioProveedor> precioItem;
    protected TipoDeIVA tipoDeIVA;
    protected Rubro rubro;

    public Item(String descripcionDeItem, UnidadDeMedida  unidadMedida, TipoDeIVA tipoDeIVA, Rubro rubro) {
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
        return "";
    }

    public void agregarPrecio(PrecioProveedor precio) {
        this.precioItem.add(precio);
    }
}