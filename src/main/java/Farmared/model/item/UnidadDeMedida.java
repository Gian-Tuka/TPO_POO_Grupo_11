package Farmared.model.item;

import java.util.UUID;

public class UnidadDeMedida {
    private String codigoUnidad;
    private String descripcionUnidad;
    private TipoDeUnidad tipoDeUnidad;


    public UnidadDeMedida(String descripcionUnidad, TipoDeUnidad tipoDeUnidad) {
        this.codigoUnidad = UUID.randomUUID().toString();
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
}