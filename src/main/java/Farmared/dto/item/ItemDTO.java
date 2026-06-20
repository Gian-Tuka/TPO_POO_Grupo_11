package Farmared.dto.item;

public class ItemDTO {
    private String codigo;
    private String descripcionDeItem;
    private String unidadMedida;
    private String tipoDeIVA;
    private String rubro;
    private String precioVigente;

    public ItemDTO(String codigo, String descripcionDeItem, String unidadMedida, String tipoDeIVA, String rubro, String precioVigente) {
        this.codigo = codigo;
        this.descripcionDeItem = descripcionDeItem;
        this.unidadMedida = unidadMedida;
        this.tipoDeIVA = tipoDeIVA;
        this.rubro = rubro;
        this.precioVigente = precioVigente;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcionDeItem() {
        return descripcionDeItem;
    }

    public void setDescripcionDeItem(String descripcionDeItem) {
        this.descripcionDeItem = descripcionDeItem;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public String getPrecioItem() {
        return precioVigente;
    }

    public void setPrecioItem(String precioItem) {
        this.precioVigente = precioItem;
    }

    public String getTipoDeIVA() {
        return tipoDeIVA;
    }

    public void setTipoDeIVA(String tipoDeIVA) {
        this.tipoDeIVA = tipoDeIVA;
    }

    public String getRubro() {
        return rubro;
    }

    public void setRubro(String rubro) {
        this.rubro = rubro;
    }

}
