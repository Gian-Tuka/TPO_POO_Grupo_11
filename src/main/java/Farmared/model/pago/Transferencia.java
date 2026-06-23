package Farmared.model.pago;

import java.util.Date;

public class Transferencia extends FormaDePago {
    private String cbu;
    private String bancoDestino;
    private String nroTransferencia;

    public Transferencia(float monto, Date fecha, String cbu,
                         String bancoDestino, String nroTransferencia) {
        super(monto, fecha);
        this.cbu = cbu;
        this.bancoDestino = bancoDestino;
        this.nroTransferencia = nroTransferencia;
    }

    // Getters
    public String getCbu() {
        return cbu;
    }

    public String getBancoDestino() {
        return bancoDestino;
    }

    public String getNroTransferencia() {
        return nroTransferencia;
    }

    // Setters
    public void setCbu(String cbu) {
        this.cbu = cbu;
    }

    public void setBancoDestino(String bancoDestino) {
        this.bancoDestino = bancoDestino;
    }

    public void setNroTransferencia(String nroTransferencia) {
        this.nroTransferencia = nroTransferencia;
    }
}
