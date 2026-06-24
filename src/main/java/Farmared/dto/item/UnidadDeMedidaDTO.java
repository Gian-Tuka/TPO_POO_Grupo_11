package Farmared.dto.item;

public class UnidadDeMedidaDTO {
    private String codigoUnidad;
    private String descripcionUnidad;
    private String tipoDeUnidad;

    // Alta desde la vista
    public UnidadDeMedidaDTO(String descripcionUnidad, String tipoDeUnidad) {
        this.descripcionUnidad = descripcionUnidad;
        this.tipoDeUnidad = tipoDeUnidad;
    }

    // Para popular grillas y combos
    public UnidadDeMedidaDTO(String codigoUnidad, String descripcionUnidad, String tipoDeUnidad) {
        this.codigoUnidad = codigoUnidad;
        this.descripcionUnidad = descripcionUnidad;
        this.tipoDeUnidad = tipoDeUnidad;
    }

    public String getCodigoUnidad() {
        return codigoUnidad;
    }

    public void setCodigoUnidad(String codigoUnidad) {
        this.codigoUnidad = codigoUnidad;
    }

    public String getDescripcionUnidad() {
        return descripcionUnidad;
    }

    public void setDescripcionUnidad(String descripcionUnidad) {
        this.descripcionUnidad = descripcionUnidad;
    }

    public String getTipoDeUnidad() {
        return tipoDeUnidad;
    }

    public void setTipoDeUnidad(String tipoDeUnidad) {
        this.tipoDeUnidad = tipoDeUnidad;
    }

    @Override
    public String toString() {
        return descripcionUnidad + " (" + tipoDeUnidad + ")"; // Formato para el combobox
    }
}
