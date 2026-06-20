package Farmared.model.item;

import Farmared.utils.GeneradorDeCodigos;

import java.util.UUID;

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
}