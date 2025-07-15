package br.com.rodoviaria.spring_clean_arch.domain.valueobjects;

import br.com.rodoviaria.spring_clean_arch.application.ports.out.senha.SenhaEncoderPort;

public final class Senha {
    private final String senhaHash;
    private static final String REGEX = "^(?=.*[!@#$%^&*()_+=\\-{}\\[\\]:;\"'<>,.?/])(?=.{6,}).*$";

    // Construtor privado para senhas já codificadas (carregadas do banco)
    private Senha(String senhaHash) {
        if (senhaHash == null || senhaHash.isBlank()) {
            throw new IllegalArgumentException("Hash da senha não pode ser nulo ou vazio.");
        }
        this.senhaHash = senhaHash;
    }

    // Método para criar uma nova senha (codificada)
    public static Senha criar(String senhaBruta, SenhaEncoderPort encoder) {
        validarFormato(senhaBruta);
        String senhaCodificada = encoder.encode(senhaBruta);
        return new Senha(senhaCodificada);
    }

    // Método para carregar um hash do banco
    public static Senha carregar(String senhaHash) {
        return new Senha(senhaHash);
    }

    // Valida o formato da senha em texto puro
    public static void validarFormato(String senhaBruta) {
        if (senhaBruta == null || !senhaBruta.matches(REGEX)) {
            throw new IllegalArgumentException("Senha inválida. Mínimo 6 caracteres e pelo menos 1 caractere especial.");
        }
    }

    public String getSenhaHash() {
        return senhaHash;
    }
}