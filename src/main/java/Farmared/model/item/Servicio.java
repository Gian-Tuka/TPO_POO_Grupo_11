package Farmared.model.item;
import Farmared.model.precio.PrecioProveedor;
import Farmared.model.rubro.Rubro;
import java.util.ArrayList;



public class Servicio extends Item {

    public Servicio(String descripcionDeItem, UnidadDeMedida unidadMedida,
                    ArrayList<PrecioProveedor> precioItem, TipoDeIVA tipoDeIVA, Rubro rubro) {

        // Llama al constructor de la clase padre (Item)
        super(descripcionDeItem, unidadMedida, tipoDeIVA, rubro, precioItem);

    }
}