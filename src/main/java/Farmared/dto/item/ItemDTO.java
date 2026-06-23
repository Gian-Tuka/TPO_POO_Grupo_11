package Farmared.dto.item;

public class ItemDTO {
    private String codigo;
    private String descripcionDeItem;
    private String descripcionUnidadMedida;
    private String tipoUDM;
    private String tipoDeIVA;
    private String rubro;
    private String precioVigente;
    private String tipoItem;

    public ItemDTO(String codigo, String descripcionDeItem, String descripcionUDM, String tipoUDM, String tipoDeIVA, String rubro, String precioVigente) {
        this.codigo = codigo;
        this.descripcionDeItem = descripcionDeItem;
        this.descripcionUnidadMedida = descripcionUDM;
        this.tipoUDM = tipoUDM;
        this.tipoDeIVA = tipoDeIVA;
        this.rubro = rubro;
        this.precioVigente = precioVigente;
    }

    public ItemDTO(String descripcionDeItem, String descripcionUDM, String tipoUDM, String tipoDeIVA, String rubro) {
        this.descripcionDeItem = descripcionDeItem;
        this.descripcionUnidadMedida = descripcionUDM;
        this.tipoUDM = tipoUDM;
        this.tipoDeIVA = tipoDeIVA;
        this.rubro = rubro;
        this.precioVigente = precioVigente;
    }

    public String getTipoItem() {
        return tipoItem;
    }

    public void setTipoItem(String tipoItem) {}

    public String getPrecioVigente() {
        return precioVigente;
    }

    public void setPrecioVigente(String precioVigente) {
        this.precioVigente = precioVigente;
    }

    public String getDescripcionUnidadMedida() {
        return descripcionUnidadMedida;
    }
    public String getTipoUDM() {
        return tipoUDM;
    }

    public void setTipoUDM(String tipoUDM) {
        this.tipoUDM = tipoUDM;
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
