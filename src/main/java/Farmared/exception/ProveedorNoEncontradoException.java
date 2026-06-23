package Farmared.exception;

public class ProveedorNoEncontradoException extends FarmaredException {
    public ProveedorNoEncontradoException(String cuit) {
        super("Proveedor no encontrado con CUIT: " + cuit);
    }
}
