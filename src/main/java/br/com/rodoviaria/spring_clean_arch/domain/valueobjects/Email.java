package br.com.rodoviaria.spring_clean_arch.domain.valueobjects;

public class Email {
    private final String email;
    private static final String REGEX = "^[a-zA-Z0-9._%+-]+@(gmail|outlook)\\.com$";

    public Email(String email) {
        if (!email.matches(REGEX)) {
            throw new IllegalArgumentException("Email inválido. Use apenas @gmail.com ou @outlook.com.");
        }
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
