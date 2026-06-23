package Farmared.model.item;

import Farmared.utils.GeneradorDeCodigos;

public class UnidadDeMedida {
    private String codigoUnidad;
    private String descripcionUnidad;
    private TipoDeUnidad tipoDeUnidad;


    public UnidadDeMedida(String descripcionUnidad, TipoDeUnidad tipoDeUnidad) {
        this.codigoUnidad = generarCod();
        this.descripcionUnidad = descripcionUnidad;
        this.tipoDeUnidad = tipoDeUnidad;
    }


    public String getCodigoUnidad() {
        return codigoUnidad;
    }

    public TipoDeUnidad getTipoDeUnidad() {
        return tipoDeUnidad;
    }

    public String getDescripcionUnidad() {
        return descripcionUnidad;
    }

    private String generarCod() {
        GeneradorDeCodigos gdc = new GeneradorDeCodigos();

        return gdc.generarCodigo("UDM", 5);
    }

    // Setters faltantes (sección 1.1)
    public void setDescripcionUnidad(String descripcionUnidad) { this.descripcionUnidad = descripcionUnidad; }
    public void setTipoDeUnidad(TipoDeUnidad tipoDeUnidad) { this.tipoDeUnidad = tipoDeUnidad; }
}