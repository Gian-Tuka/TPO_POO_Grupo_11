package Farmared.model.rubro;

import java.util.UUID;

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
        return UUID.randomUUID().toString();
    }

    public String getIdRubro() { return idRubro; }
    public String getNombreRubro() { return nombreRubro; }
    public TipoRubro getTipoRubro() { return tipoRubro; }
}
