package Farmared.exception;

public class ItemNoEncontradoException extends FarmaredException {
    public ItemNoEncontradoException(String codigo) {
        super("Ítem no encontrado con código: " + codigo);
    }
}
