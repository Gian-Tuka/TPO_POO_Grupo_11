package Farmared.model.ordenCompra;

import Farmared.model.item.Item;
import Farmared.model.proveedor.Proveedor;
import Farmared.model.user.Usuario;
import Farmared.utils.GeneradorDeCodigos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrdenDeCompra {
    private String nroOC;
    private Date fechaEmision;
    private Proveedor proveedor;
    private List<DetalleOC> detalles;
    private Float precioTotalOC;
    private EstadoOC estado;
    private Usuario creador;
    private Autorizacion autorizacion;

    public OrdenDeCompra(Proveedor proveedor) {
        this.proveedor = proveedor;
        this.detalles = new ArrayList<>();
        this.precioTotalOC = 0f;
        this.nroOC = crearNroOC();
        this.fechaEmision = obtenerFecha();
    }

    private String crearNroOC() {
        GeneradorDeCodigos gdc = new GeneradorDeCodigos();
        return gdc.generarCodigo("OC", 6);
    }

    private Date obtenerFecha() {
        return new Date();
    }

    public DetalleOC crearDetalle(Item item, int cantidad) {
        DetalleOC detalle = new DetalleOC(item, cantidad);
        detalle.obtenerPrecioProveedor(this.proveedor);
        this.detalles.add(detalle);
        calcularTotalOC();
        return detalle;
    }

    private Float calcularTotalOC() {
        float total = 0f;
        for (DetalleOC det : detalles) {
            total += det.getSubtotalPorItem();
        }
        this.precioTotalOC = total;
        return total;
    }

    public void settearEstado(EstadoOC estado) {
        this.estado = estado;
    }

    public Usuario obtenerCreador() {
        return this.creador;
    }

    public List<OrdenDeCompra> reporteOC() {
        List<OrdenDeCompra> list = new ArrayList<>();
        list.add(this);
        return list;
    }

    public Float obtenerTotalOC() {
        return this.precioTotalOC;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public String getNroOC() {
        return nroOC;
    }

    public EstadoOC getEstado() {
        return estado;
    }

    public void setCreador(Usuario creador) {
        this.creador = creador;
    }

    public Autorizacion getAutorizacion() {
        return autorizacion;
    }

    public void setAutorizacion(Autorizacion autorizacion) {
        this.autorizacion = autorizacion;
    }
    
    public List<DetalleOC> getDetalles() {
        return detalles;
    }
}
