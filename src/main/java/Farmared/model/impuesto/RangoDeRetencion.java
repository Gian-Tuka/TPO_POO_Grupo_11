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

    // Sección 1.4 — Bug 9: Lógica real implementada (antes retornaba null)
    public boolean estaEnRango(float monto) {
        return monto >= minimo && monto <= maximo;
    }

    // Sección 1.4 — Bug 9: Lógica real implementada (antes retornaba null)
    public float calcularRetencion(float monto) {
        return monto * (retencion / 100f);
    }

    // Getters faltantes (sección 1.3)
    public float getMinimo() { return minimo; }
    public float getMaximo() { return maximo; }
    public float getRetencion() { return retencion; }
}

