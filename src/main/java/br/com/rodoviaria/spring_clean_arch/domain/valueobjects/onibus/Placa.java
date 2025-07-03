package br.com.rodoviaria.spring_clean_arch.domain.valueobjects.onibus;

import br.com.rodoviaria.spring_clean_arch.domain.exceptions.onibus.PlacaInvalidaException;

import java.util.regex.Pattern;

public class Placa {
    private final String valor;

    // REGEX que valida os dois formatos: "AAA-9999" ou "AAA9A99"
    private static final Pattern REGEX_PLACA = Pattern.compile("^[A-Z]{3}-?\\d[A-Z0-9]\\d{2}$");

    public Placa(String valor) {
        if (valor == null || !REGEX_PLACA.matcher(valor.toUpperCase()).matches()) {
            throw new PlacaInvalidaException("Formato da placa inválido: " + valor);
        }
        // Armazena a placa em um formato padronizado (ex: sem o hífen)
        this.valor = valor.toUpperCase().replace("-", "");
    }

    public String getValor() {
        return valor;
    }

    // Você pode adicionar métodos úteis, como um que retorna a placa formatada
    public String getValorFormatado() {
        if (valor.matches("^[A-Z]{3}\\d{4}$")) { // Formato antigo
            return valor.substring(0, 3) + "-" + valor.substring(3);
        }
        return valor; // Formato Mercosul já está sem hífen
    }
}
