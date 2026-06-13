package Farmared.controller.proveedores;

import Farmared.model.item.Item;
import Farmared.model.proveedor.Proveedor;
import Farmared.model.rubro.Rubro;

import java.util.ArrayList;

public class ControladorProveedores {

    private static ControladorProveedores instance = null;

    private ArrayList<Proveedor> proveedores;
    private ArrayList<Rubro> rubros;

    private ControladorProveedores() {
        this.proveedores = new ArrayList<>();
        this.rubros = new ArrayList<>();
    }

    public synchronized static ControladorProveedores getInstance() {
        if (instance == null) {
            instance = new ControladorProveedores();
        }
        return instance;
    }

//    public void asiciarPrecioAProveedor(String cuitProveedor, String codigoItem, float precio) {
//        Proveedor proveedor = buscaProveedor(cuitProveedor);
//
//        Item item =
//    }

    public Proveedor buscaProveedor(String cuitProveedor) {
        for (int i = 0; i < this.proveedores.size(); i++) {
            if (this.proveedores.get(i).getCuit().equals(cuitProveedor)) {
                return this.proveedores.get(i);
            }
        }
        return null;
    }
}
