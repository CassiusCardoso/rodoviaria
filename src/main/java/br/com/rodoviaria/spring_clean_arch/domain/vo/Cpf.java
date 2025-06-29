package br.com.rodoviaria.spring_clean_arch.domain.vo;

public class Cpf {
    private final String cpf;
    private static final String REGEX = "^(\\d{3}\\.?\\d{3}\\.?\\d{3}\\-?\\d{2})$";

    public Cpf(String cpf) {
        if (!cpf.matches(REGEX)) {
            throw new IllegalArgumentException("Formato de CPF inválido: " + cpf);
        }

        String cpfNumeros = cpf.replaceAll("[^\\d]", "");

        if (!isCpfValido(cpfNumeros)) {
            throw new IllegalArgumentException("CPF inválido: " + cpf);
        }

        this.cpf = cpfNumeros;
    }

    public String getCpf() {
        return cpf;
    }

    private boolean isCpfValido(String cpf) {
        if (cpf.length() != 11 || cpf.chars().distinct().count() == 1) return false;

        try {
            int soma = 0;
            for (int i = 0; i < 9; i++) {
                soma += (cpf.charAt(i) - '0') * (10 - i);
            }
            int digito1 = 11 - (soma % 11);
            if (digito1 >= 10) digito1 = 0;
            if (digito1 != cpf.charAt(9) - '0') return false;

            soma = 0;
            for (int i = 0; i < 10; i++) {
                soma += (cpf.charAt(i) - '0') * (11 - i);
            }
            int digito2 = 11 - (soma % 11);
            if (digito2 >= 10) digito2 = 0;
            return digito2 == cpf.charAt(10) - '0';
        } catch (Exception e) {
            return false;
        }
    }
}
