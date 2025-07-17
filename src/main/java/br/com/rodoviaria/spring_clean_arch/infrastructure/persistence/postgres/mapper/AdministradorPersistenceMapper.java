// Conteúdo para AdministradorPersistenceMapper.java
package br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.mapper;

import br.com.rodoviaria.spring_clean_arch.domain.entities.Administrador;
import br.com.rodoviaria.spring_clean_arch.domain.valueobjects.Email;
import br.com.rodoviaria.spring_clean_arch.domain.valueobjects.Senha;
import br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.model.AdministradorModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AdministradorPersistenceMapper {
    AdministradorModel toModel(Administrador administrador);
    Administrador toDomain(AdministradorModel model);

    default String mapEmailToString(Email email) {
        return email == null ? null : email.getEmail();
    }

    default Email mapStringToEmail(String email) {
        return email == null ? null : new Email(email);
    }

    default String mapSenhaToString(Senha senha) {
        return senha == null ? null : senha.getSenhaHash();
    }

    // --- CORREÇÃO AQUI ---
    default Senha mapStringToSenha(String senha) {
        // Usa o método de fábrica para carregar o hash sem validar o formato.
        return senha == null ? null : Senha.carregar(senha);
    }
}