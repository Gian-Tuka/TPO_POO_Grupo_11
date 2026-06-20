package Farmared.utils;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class GeneradorDeCodigos {

    private static final Set<String> codigosExistentes = new HashSet<String>();

    public String generarCodigo(String codigoAlfabetico, int cantidadDeNumeros) {
        String nuevoCodigo = "";

        do {
            nuevoCodigo = generarCodigoInterno(codigoAlfabetico, cantidadDeNumeros);
        } while (codigosExistentes.contains(nuevoCodigo));

        codigosExistentes.add(nuevoCodigo);
        return nuevoCodigo;
    }

    private String generarCodigoInterno(String parteAlfabetica, int cantidadNumerica) {
        String codigoNumerico = "";
        Random random = new Random();

        for (int i = 0; i < cantidadNumerica; i++) {
            int numero = random.nextInt(10);
            codigoNumerico = codigoNumerico + numero;
        }

        return parteAlfabetica + "-" + codigoNumerico;
    }


}


