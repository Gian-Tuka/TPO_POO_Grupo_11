package Farmared.model.proveedor;

import Farmared.model.cuentaCorriente.CuentaCorriente;
import Farmared.model.impuesto.CertificadoNoRetencion;
import Farmared.model.impuesto.ImpuestoRetenible;
import Farmared.model.precio.PrecioProveedor;
import Farmared.model.rubro.Rubro;
import Farmared.utils.Domicilio;

import java.util.ArrayList;
import java.util.Date;

public class Proveedor {

    private String cuit;
    private String razonSocial;
    private String nombreFantasia;
    private Domicilio domicilioComercial;
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


    public Proveedor(String cuit, String razonSocial, String nombreFantasia, Domicilio domicilioComercial,
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


    public String getCuit() {
        return cuit;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public String getNombreFantasia() {
        return nombreFantasia;
    }

    public Domicilio getDomicilioComercial() {
        return domicilioComercial;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public CondicionIVA getCondicionIVA() {
        return condicionIVA;
    }

    public String getNroIngBru() {
        return nroIngBru;
    }

    public ArrayList<ImpuestoRetenible> getImpuestos() {
        return impuestos;
    }

    public Date getFechaInicioActividades() {
        return fechaInicioActividades;
    }

    public ArrayList<CertificadoNoRetencion> getCertificadosNoRet() {
        return certificadosNoRet;
    }

    public ArrayList<Rubro> getRubroProveedor() {
        return rubroProveedor;
    }

    public CuentaCorriente getCuentaCorriente() {
        return cuentaCorriente;
    }

    public ArrayList<PrecioProveedor> getPrecioPorItem() {
        return precioPorItem;
    }
}



