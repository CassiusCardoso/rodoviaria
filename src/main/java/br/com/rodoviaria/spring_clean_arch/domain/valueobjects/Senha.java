package br.com.rodoviaria.spring_clean_arch.domain.valueobjects;

public final class Senha {
    private final String senhaBruta;
    private static final String REGEX = "^(?=.*[!@#$%^&*()_+=\\-{}\\[\\]:;\"'<>,.?/])(?=.{6,}).*$";

    public Senha(String senha) {
        if (senha == null || !senha.matches(REGEX)) {
            throw new IllegalArgumentException("Senha inválida. Mínimo 6 caracteres e pelo menos 1 caractere especial.");
        }
        this.senhaBruta = senha;
    }

    public String getSenhaBruta() {
        return senhaBruta;
    }
}
