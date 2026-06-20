package Farmared.utils;

public class Validations {




    //metodo para validar cuit
    public Boolean validCuit(String cuilCuit) {
        if (cuilCuit == null) return false;

        // Eliminar guiones y verificar longitud exacta de 11 dígitos
        String cuitLimpio = cuilCuit.replaceAll("-", "");
        if (cuitLimpio.length() != 11 || !cuitLimpio.matches("\\d+")) {
            return false;
        }

        char[] chars = cuitLimpio.toCharArray();
        int[] verificador = {5, 4, 3, 2, 7, 6, 5, 4, 3, 2};
        int suma = 0;

        // Calcular la suma de los primeros 10 dígitos multiplicados por los coeficientes
        for (int i = 0; i < 10; i++) {
            suma += Character.getNumericValue(chars[i]) * verificador[i];
        }

        // Obtener el resto y calcular el dígito verificador
        int resto = suma % 11;
        int digitoCalculado = 11 - resto;

        if (digitoCalculado == 11) {
            digitoCalculado = 0;
        } else if (digitoCalculado == 10) {
            return false; // Por definición, un CUIT nunca termina en 10
        }

        // Comparar con el último dígito del CUIT original
        int digitoEsperado = Character.getNumericValue(chars[10]);
        return digitoCalculado == digitoEsperado;
    }

}
