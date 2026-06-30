package Farmared.model.pago;

import java.util.Date;

public class Transferencia extends FormaDePago {
    private String cbu;
    private String bancoDestino;

    public Transferencia(float monto, String cbu, String bancoDestino) {
        super(monto);
        this.cbu = cbu;
        this.bancoDestino = bancoDestino;
    }

    // Getters
    public String getCbu() {
        return cbu;
    }

    public String getBancoDestino() {
        return bancoDestino;
    }

    public void setBancoDestino(String bancoDestino) {
        this.bancoDestino = bancoDestino;
    }

}
