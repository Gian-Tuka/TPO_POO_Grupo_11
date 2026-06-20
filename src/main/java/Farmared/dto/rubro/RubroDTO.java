package Farmared.dto.rubro;

public class RubroDTO {
     private String id;
     private String nombre;
     private String tipoRubro;

    public RubroDTO(String  id, String nombre, String tipoRubro) {
        this.id = id;
        this.nombre = nombre;
        this.tipoRubro = tipoRubro;
    }


    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipoRubro() {
        return tipoRubro;
    }

}
