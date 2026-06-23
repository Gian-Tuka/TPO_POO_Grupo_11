package Farmared.model.rubro;

import Farmared.utils.GeneradorDeCodigos;
import java.util.Objects;

public class Rubro {
    private String idRubro;
    private String nombreRubro;
    private TipoRubro tipoRubro; //Bienes o servicios

    public Rubro(String nombreRubro, TipoRubro tipoRubro) {
        this.idRubro = generateIDRubro();
        this.nombreRubro = nombreRubro;
        this.tipoRubro = tipoRubro;
    }

    private String generateIDRubro() {
        GeneradorDeCodigos gdc = new GeneradorDeCodigos();
        return gdc.generarCodigo("RBR", 5);
    }

    public String getIdRubro() { return idRubro; }
    public String getNombreRubro() { return nombreRubro; }
    public TipoRubro getTipoRubro() { return tipoRubro; }

    // Setters faltantes (sección 1.1)
    public void setNombreRubro(String nombreRubro) { this.nombreRubro = nombreRubro; }
    public void setTipoRubro(TipoRubro tipoRubro) { this.tipoRubro = tipoRubro; }

    // equals/hashCode basado en idRubro (sección 1.2)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rubro rubro = (Rubro) o;
        return Objects.equals(idRubro, rubro.idRubro);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idRubro);
    }
}
