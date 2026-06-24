package Farmared.dto.ordenes;

import java.util.ArrayList;
import java.util.List;

public class OrdenDeCompraDTO {
    private String nroOC;
    private String fechaEmision;
    private String cuitProveedor;
    private String razonSocialProveedor;
    private String estado;
    private float total;
    private String creadorLegajo;
    private List<DetalleOCDTO> detalles;
    private List<DetalleItemDTO> items;

    // Constructor para la creación (desde la GUI)
    public OrdenDeCompraDTO(String cuitProveedor, List<DetalleItemDTO> items) {
        this.cuitProveedor = cuitProveedor;
        this.items = items;
        this.detalles = new ArrayList<>();
    }

    // Constructor para visualización / retorno
    public OrdenDeCompraDTO(String nroOC, String fechaEmision, String cuitProveedor, String razonSocialProveedor,
                             String estado, float total, String creadorLegajo, List<DetalleOCDTO> detalles) {
        this.nroOC = nroOC;
        this.fechaEmision = fechaEmision;
        this.cuitProveedor = cuitProveedor;
        this.razonSocialProveedor = razonSocialProveedor;
        this.estado = estado;
        this.total = total;
        this.creadorLegajo = creadorLegajo;
        this.detalles = detalles;
        this.items = new ArrayList<>();
    }

    public String getNroOC() {
        return nroOC;
    }

    public void setNroOC(String nroOC) {
        this.nroOC = nroOC;
    }

    public String getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(String fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getCuitProveedor() {
        return cuitProveedor;
    }

    public void setCuitProveedor(String cuitProveedor) {
        this.cuitProveedor = cuitProveedor;
    }

    public String getRazonSocialProveedor() {
        return razonSocialProveedor;
    }

    public void setRazonSocialProveedor(String razonSocialProveedor) {
        this.razonSocialProveedor = razonSocialProveedor;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public String getCreadorLegajo() {
        return creadorLegajo;
    }

    public void setCreadorLegajo(String creadorLegajo) {
        this.creadorLegajo = creadorLegajo;
    }

    public List<DetalleOCDTO> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleOCDTO> detalles) {
        this.detalles = detalles;
    }

    public List<DetalleItemDTO> getItems() {
        return items;
    }

    public void setItems(List<DetalleItemDTO> items) {
        this.items = items;
    }
}
