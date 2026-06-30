package Farmared.controller.impuestos;

import Farmared.dto.impuesto.ImpuestoRetenibleDTO;
import Farmared.model.impuesto.ImpuestoRetenible;

import java.util.ArrayList;

public class ControladorImpuestos {
    private static ControladorImpuestos instance = null;
    private ArrayList<ImpuestoRetenible> impuestos;

    private ControladorImpuestos() {
        this.impuestos = new ArrayList<>();
        cargarDatosSimulados();
    }

    public synchronized static ControladorImpuestos getInstance() {
        if (instance == null) {
            instance = new ControladorImpuestos();
        }
        return instance;
    }

    public ArrayList<ImpuestoRetenibleDTO> obtenerImpuestosDTO() {
        ArrayList<ImpuestoRetenibleDTO> lista = new ArrayList<>();
        for (ImpuestoRetenible imp : impuestos) {
            lista.add(toDTO(imp));
        }
        return lista;
    }

    public ImpuestoRetenible buscarImpuestoPorNro(String nroRetencion) {
        for (ImpuestoRetenible imp : impuestos) {
            if (imp.getNroRetencion().equals(nroRetencion)) {
                return imp;
            }
        }
        return null;
    }

    // Método para ser llamado desde ControladorProveedores u otros
    public void altaImpuesto(ImpuestoRetenibleDTO dto) {
        ImpuestoRetenible nuevo = new ImpuestoRetenible(
                dto.getNroRetencion(),
                dto.getDescripcionRetencion(),
                Float.parseFloat(dto.getMinimoNoImponible()),
                new ArrayList<>() // rangos vacíos por ahora
        );
        this.impuestos.add(nuevo);
    }

    public void agregarRangoAImpuesto(String nroRetencion, float minimo, float maximo, float porcentaje) {
        ImpuestoRetenible imp = buscarImpuestoPorNro(nroRetencion);
        if (imp != null) {
            imp.agregarRango(new Farmared.model.impuesto.RangoDeRetencion(minimo, maximo, porcentaje));
        } else {
            throw new Farmared.exception.FarmaredException("Impuesto no encontrado: " + nroRetencion);
        }
    }

    private ImpuestoRetenibleDTO toDTO(ImpuestoRetenible model) {
        return new ImpuestoRetenibleDTO(
                model.getNroRetencion(),
                model.getDescripcionRetencion(),
                String.valueOf(model.getMinimoNoImponible())
        );
    }

    public ArrayList<ImpuestoRetenible> obtenerTodosLosImpuestosModelo(){
        return this.impuestos;
    }

    private void cargarDatosSimulados() {
        impuestos.add(new ImpuestoRetenible("RET-001", "Retención Ganancias", 150000f, new ArrayList<>()));
        impuestos.add(new ImpuestoRetenible("RET-002", "Retención IIBB", 50000f, new ArrayList<>()));
        impuestos.add(new ImpuestoRetenible("RET-003", "Retención IVA", 10000f, new ArrayList<>()));
    }
}
