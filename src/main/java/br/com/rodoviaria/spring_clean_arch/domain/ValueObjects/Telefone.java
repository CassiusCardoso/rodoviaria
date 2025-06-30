package br.com.rodoviaria.spring_clean_arch.domain.ValueObjects;

public class Telefone {
    private final String telefone;
    private static final String REGEX = "^\\(\\d{2}\\)\\s?9\\d{4}-\\d{4}$";

    public Telefone(String telefone) {
        if (!telefone.matches(REGEX)) {
            throw new IllegalArgumentException("Telefone inválido. Use o formato (XX) 9XXXX-XXXX.");
        }
        this.telefone = telefone;
    }

    public String getTelefone() {
        return telefone;
    }
}
