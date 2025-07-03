package br.com.rodoviaria.spring_clean_arch.domain.valueobjects.passageiro;

public final class Senha {
    private final String senhaHash;
    private static final String REGEX = "^(?=.*[!@#$%^&*()_+=\\-{}\\[\\]:;\"'<>,.?/])(?=.{6,}).*$";

    public Senha(String senha) {
        if (senha == null || !senha.matches(REGEX)) {
            throw new IllegalArgumentException("Senha inválida. Mínimo 6 caracteres e pelo menos 1 caractere especial.");
        }
        this.senhaHash = senha;
    }

    public String getSenhaHash() {
        return senhaHash;
    }
}
