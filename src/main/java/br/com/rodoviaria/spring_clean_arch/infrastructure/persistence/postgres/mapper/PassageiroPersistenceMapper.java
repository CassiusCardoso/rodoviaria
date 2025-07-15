package br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.mapper;

import br.com.rodoviaria.spring_clean_arch.domain.entities.Passageiro;
import br.com.rodoviaria.spring_clean_arch.domain.valueobjects.Cpf;
import br.com.rodoviaria.spring_clean_arch.domain.valueobjects.Email;
import br.com.rodoviaria.spring_clean_arch.domain.valueobjects.Senha;
import br.com.rodoviaria.spring_clean_arch.domain.valueobjects.Telefone;
import br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.model.PassageiroModel;
import org.mapstruct.Mapper;

@Mapper
public interface PassageiroPersistenceMapper {
    PassageiroModel toModel(Passageiro passageiro);
    Passageiro toDomain(PassageiroModel model);

    default String mapCpfToString(Cpf cpf) { return cpf == null ? null : cpf.getCpf(); }
    default Cpf mapStringToCpf(String cpf) { return cpf == null ? null : new Cpf(cpf); }

    default String mapEmailToString(Email email) { return email == null ? null : email.getEmail(); }
    default Email mapStringToEmail(String email) { return email == null ? null : new Email(email); }

    default String mapSenhaToString(Senha senha) { return senha == null ? null : senha.getSenhaHash(); }

    // --- CORREÇÃO AQUI ---
    default Senha mapStringToSenha(String senha) {
        return senha == null ? null : Senha.carregar(senha);
    }

    default String mapTelefoneToString(Telefone telefone) { return telefone == null ? null : telefone.getTelefone(); }
    default Telefone mapStringToTelefone(String telefone) { return telefone == null ? null : new Telefone(telefone); }
}