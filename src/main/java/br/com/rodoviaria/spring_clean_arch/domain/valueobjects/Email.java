package br.com.rodoviaria.spring_clean_arch.domain.valueobjects;

public class Email {
    private final String email;
    // REGEX padrão para validar a maioria dos formatos de e-mail
    private static final String REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";

    public Email(String email) {
        if (!email.matches(REGEX)) {
            throw new IllegalArgumentException("Email inválido. Use apenas @gmail.com ou @outlook.com.");        }
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
