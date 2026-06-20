package Farmared.model.item;

import Farmared.model.precio.PrecioProveedor;
import Farmared.model.rubro.Rubro;
import java.util.ArrayList;


public class Producto extends Item {


    public Producto(String descripcionDeItem, UnidadDeMedida unidadMedida, TipoDeIVA tipoDeIVA, Rubro rubro) {

        // Llama al constructor de la clase padre (Item)
        super(descripcionDeItem, unidadMedida, tipoDeIVA, rubro);

    }
}
