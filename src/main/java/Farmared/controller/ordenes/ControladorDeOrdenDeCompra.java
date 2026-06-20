package Farmared.controller.ordenes;

import Farmared.controller.proveedores.ControladorProveedores;
import Farmared.controller.usuariosYSeguridad.ControladorUsuariosYSeguridad;
import Farmared.model.cuentaCorriente.CuentaCorriente;
import Farmared.model.ordenCompra.EstadoOC;
import Farmared.model.ordenCompra.OrdenDeCompra;
import Farmared.model.proveedor.Proveedor;

import java.util.ArrayList;
import java.util.List;

public class ControladorDeOrdenDeCompra {
    private static ControladorDeOrdenDeCompra instance = null;

    private List<OrdenDeCompra> ordenesDeCompra;
    private List<EstadoOC> estadosDeOC;

    private ControladorDeOrdenDeCompra() {
        this.ordenesDeCompra = new ArrayList<>();
        this.estadosDeOC = new ArrayList<>();
    }

    public synchronized static ControladorDeOrdenDeCompra getInstance() {
        if (instance == null) {
            instance = new ControladorDeOrdenDeCompra();
        }
        return instance;
    }

    public void emitirOC(Proveedor proveedor) {
        ControladorUsuariosYSeguridad controlUsuarios = ControladorUsuariosYSeguridad.getInstance();
        ControladorProveedores controlProveedores = ControladorProveedores.getInstance();

        // obtenerUsuarioActual no está estandarizado en la vista todavía, 
        // pero simulamos la secuencia:
        // Usuario actual = controlUsuarios.obtenerUsuarioActual();

        if (existeProveedor(proveedor)) {
            OrdenDeCompra oc = new OrdenDeCompra(proveedor);
            
            // En la vida real aquí se añadirían detalles (crearDetalle) desde la GUI
            // oc.crearDetalle(item, cantidad);
            
            Float totalOC = obtenerTotalOC(oc);
            
            añadirOC(oc);

            boolean limiteOK = validarLimite(proveedor, totalOC);
            if (limiteOK) {
                oc.settearEstado(EstadoOC.APROBADA);
            } else {
                oc.settearEstado(EstadoOC.PENDIENTE_AUTORIZACION);
            }
        } else {
            System.out.println("Error: El proveedor no existe.");
        }
    }

    public Boolean existeProveedor(Proveedor proveedor) {
        return ControladorProveedores.getInstance().existeProveedor(proveedor);
    }

    public void añadirOC(OrdenDeCompra oc) {
        this.ordenesDeCompra.add(oc);
    }

    public Float obtenerTotalOC(OrdenDeCompra oc) {
        return oc.obtenerTotalOC();
    }

    public Boolean validarLimite(Proveedor proveedor, Float total) {
        CuentaCorriente cc = ControladorProveedores.getInstance().cuentaCorriente(proveedor);
        if (cc != null) {
            float deudaActual = cc.getDeudaActual();
            float topeDeuda = cc.getTopeDeuda();
            return (deudaActual + total) <= topeDeuda;
        }
        return false;
    }

    public List<OrdenDeCompra> getOrdenesDeCompra() {
        return ordenesDeCompra;
    }
}
