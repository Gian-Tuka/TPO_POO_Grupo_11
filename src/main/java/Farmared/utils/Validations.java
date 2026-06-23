package Farmared.utils;

import Farmared.exception.NotNullException;

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

    // Valida si un String es nulo o está vacío
    public boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    // Arroja una excepción si un String es nulo o vacío
    public void requireNonEmpty(String str, String errorMessage) throws Exception {
        if (isNullOrEmpty(str)) {
            throw new Exception(errorMessage);
        }
    }

    // Verifica si un String puede ser convertido a Float
    public boolean isFloat(String str) {
        if (isNullOrEmpty(str)) return false;
        try {
            Float.parseFloat(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Verifica si un String puede ser convertido a Integer
    public boolean isInteger(String str) {
        if (isNullOrEmpty(str)) return false;
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Valida un correo electrónico
    public boolean validEmail(String email) {
        if (isNullOrEmpty(email)) return false;
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(regex);
    }

    // Valida un número de teléfono (solo números, puede contener + y -)
    public boolean validPhone(String phone) {
        if (isNullOrEmpty(phone)) return false;
        String regex = "^[+]?[0-9\\-]+$";
        return phone.matches(regex);
    }

    // Arroja una excepción si un objeto es nulo
    public void requireNonNull(Object obj, String errorMessage) {
        if (obj == null) {
            throw new NotNullException(errorMessage);
        }
    }

    // Valida que un String contenga solo letras y espacios (sin números)
    public boolean contieneSoloLetras(String str) {
        if (isNullOrEmpty(str)) return false;
        String regex = "^[A-Za-zÁ-Úá-úñÑ ]+$";
        return str.matches(regex);
    }

}
