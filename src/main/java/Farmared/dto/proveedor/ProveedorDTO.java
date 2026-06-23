package Farmared.dto.proveedor;



import java.util.ArrayList;

public class ProveedorDTO {
    private String cuit;
    private String razonSocial;
    private String nombreFantasia;

    private String calle;
    private String numeroDpto;
    private String codigoPostal;
    private String ciudad;
    private String pais;

    private String telefono;
    private String correo;
    private String condicionIVA;
    private String nroIngBru;
    private String fechaInicioActividades;
    private float topeDeuda;

    private ArrayList<String> idsRubros;

    public ProveedorDTO(String cuit, String razonSocial, String nombreFantasia,
                        String calle, String numeroDpto, String codigoPostal, String ciudad, String pais,
                        String telefono, String correo,
                        String condicionIVA, String nroIngBru, String fechaInicioActividades,
                        float topeDeuda, ArrayList<String> idsRubros) {
        this.cuit = cuit;
        this.razonSocial = razonSocial;
        this.nombreFantasia = nombreFantasia;

        this.calle = calle;
        this.numeroDpto = numeroDpto;
        this.codigoPostal = codigoPostal;
        this.ciudad = ciudad;
        this.pais = pais;

        this.telefono = telefono;
        this.correo = correo;
        this.condicionIVA = condicionIVA;
        this.nroIngBru = nroIngBru;
        this.fechaInicioActividades = fechaInicioActividades;
        this.topeDeuda = topeDeuda;
        this.idsRubros = idsRubros;
    }

    public String getCuit() { return cuit; }
    public String getRazonSocial() { return razonSocial; }
    public String getNombreFantasia() { return nombreFantasia; }

    public String getCalle() { return calle; }
    public String getNumeroDpto() { return numeroDpto; }
    public String getCodigoPostal() { return codigoPostal; }
    public String getCiudad() { return ciudad; }
    public String getPais() { return pais; }


    public String getTelefono() { return telefono; }
    public String getCorreo() { return correo; }
    public String getCondicionIVA() { return condicionIVA; }
    public String getNroIngBru() { return nroIngBru; }
    public String getFechaInicioActividades() { return fechaInicioActividades; }

    public float getTopeDeuda() { return topeDeuda; }
    public ArrayList<String> getIdsRubros() { return idsRubros; }

}
