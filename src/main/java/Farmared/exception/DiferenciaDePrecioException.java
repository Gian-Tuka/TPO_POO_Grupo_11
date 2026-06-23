package Farmared.exception;

public class DiferenciaDePrecioException extends FarmaredException {
    public DiferenciaDePrecioException(String codigoItem, float precioOC, float precioFacturado) {
        super("Diferencia de precio detectada en ítem " + codigoItem 
            + ". Precio OC: " + precioOC 
            + ", Precio facturado: " + precioFacturado);
    }
}
