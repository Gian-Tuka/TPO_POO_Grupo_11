package Farmared.model.proveedor;

import Farmared.model.cuentaCorriente.CuentaCorriente;
import Farmared.model.impuesto.CertificadoNoRetencion;
import Farmared.model.impuesto.ImpuestoRetenible;
import Farmared.model.precio.PrecioProveedor;
import Farmared.model.rubro.Rubro;
import Farmared.model.rubro.TipoRubro;

import java.util.ArrayList;
import java.util.Date;

public class Proveedor {

    private String cuit;
    private String razonSocial;
    private String nombreFantasia;
    private String domicilioComercial; //DEBERIA SER UNA CLASE DOMICILIO CREO Y CREAR UN DTO DE ESTO
    private String telefono;
    private String correo;
    private CondicionIVA condicionIVA;
    private String nroIngBru;
    private ArrayList<ImpuestoRetenible> impuestos;
    private Date fechaInicioActividades;
    private ArrayList<CertificadoNoRetencion> certificadosNoRet;
    private ArrayList<Rubro> rubroProveedor;
    private CuentaCorriente cuentaCorriente;
    private ArrayList<PrecioProveedor> precioPorItem;


    public Proveedor(String cuit, String razonSocial, String nombreFantasia, String domicilioComercial,
                     String telefono, String correo, CondicionIVA condicionIVA, String nroIngBru, Date fechaInicioActividades, float topeDeuda) {
        this.cuit = cuit;
        this.razonSocial = razonSocial;
        this.nombreFantasia = nombreFantasia;
        this.domicilioComercial = domicilioComercial;
        this.telefono = telefono;
        this.correo = correo;
        this.condicionIVA = condicionIVA;
        this.nroIngBru = nroIngBru;
        this.fechaInicioActividades = fechaInicioActividades;

        this.impuestos = new ArrayList<ImpuestoRetenible>();
        this.certificadosNoRet = new ArrayList<CertificadoNoRetencion>();
        this.rubroProveedor = new ArrayList<Rubro>();
        this.precioPorItem = new ArrayList<PrecioProveedor>();
        this.cuentaCorriente = new CuentaCorriente(topeDeuda);
    }

    public void asociarRubro(Rubro rubro) {
        if (!this.rubroProveedor.contains(rubro)) {
            this.rubroProveedor.add(rubro);
        }
    }

    public void agregarPrecioItem(PrecioProveedor precio) {
        this.precioPorItem.add(precio);
    }



    public String getCuit() { return cuit; }
    public String getRazonSocial() { return razonSocial; }
    public ArrayList<Rubro> getRubroProveedor() { return rubroProveedor; }
    public ArrayList<PrecioProveedor> getPrecioPorItem() { return precioPorItem; }
}



