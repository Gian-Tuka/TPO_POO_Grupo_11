package Farmared.utils;

import java.util.Random;

public class GeneradorDeCodigos {

    public String generarCodigo(String codigoAlfabetico, int cantidadDeNumeros) {
        String codigoNumerico = "";
        Random random = new Random();

        for (int i = 0; i < cantidadDeNumeros; i++) {
            int numero = random.nextInt(10);
            codigoNumerico = codigoNumerico + numero;
        }

        return codigoAlfabetico + "-" + codigoNumerico;
    }
}


