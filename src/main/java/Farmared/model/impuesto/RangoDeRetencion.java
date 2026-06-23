package Farmared.model.impuesto;

public class RangoDeRetencion {
    private float minimo;
    private float maximo;
    private float retencion;


    public RangoDeRetencion(float minimo, float maximo, float retencion) {
        this.minimo = minimo;
        this.maximo = maximo;
        this.retencion = retencion;
    }

    // Sección 1.3 — Visibilidad cambiada de private a public
    public Boolean estaEnRango(float monto){
        return null;
    }

    // Sección 1.3 — Visibilidad cambiada de private a public
    public Float calcularRetencion(float monto){
        return null;
    }

    // Getters faltantes (sección 1.3)
    public float getMinimo() { return minimo; }
    public float getMaximo() { return maximo; }
    public float getRetencion() { return retencion; }
}

