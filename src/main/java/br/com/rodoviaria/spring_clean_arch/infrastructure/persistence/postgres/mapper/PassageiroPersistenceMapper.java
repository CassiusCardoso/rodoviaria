package br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.mapper;

import br.com.rodoviaria.spring_clean_arch.domain.entities.Passageiro;
import br.com.rodoviaria.spring_clean_arch.domain.valueobjects.Cpf;
import br.com.rodoviaria.spring_clean_arch.domain.valueobjects.Email;
import br.com.rodoviaria.spring_clean_arch.domain.valueobjects.Senha;
import br.com.rodoviaria.spring_clean_arch.domain.valueobjects.Telefone;
import br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.model.PassageiroModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PassageiroPersistenceMapper {

    // MapStruct usará os métodos default abaixo para mapear os campos automaticamente
    PassageiroModel toModel(Passageiro passageiro);
    Passageiro toDomain(PassageiroModel model);

    // --- MÉTODOS DE CONVERSÃO PARA CPF ---
    default String mapCpfToString(Cpf cpf) {
        return cpf == null ? null : cpf.getCpf();
    }
    default Cpf mapStringToCpf(String cpf) {
        return cpf == null ? null : new Cpf(cpf);
    }

    // --- MÉTODOS DE CONVERSÃO PARA EMAIL ---
    default String mapEmailToString(Email email) {
        // A linha incorreta foi removida daqui.
        return email == null ? null : email.getEmail();
    }
    // Método que estava faltando foi adicionado.
    default Email mapStringToEmail(String email) {
        return email == null ? null : new Email(email);
    }

    // --- MÉTODOS DE CONVERSÃO PARA SENHA (ADICIONADOS) ---
    default String mapSenhaToString(Senha senha) {
        // Assumindo que o getter é getSenhaHash() como nos arquivos anteriores.
        return senha == null ? null : senha.getSenhaHash();
    }
    default Senha mapStringToSenha(String senha) {
        return senha == null ? null : new Senha(senha);
    }

    // --- MÉTODOS DE CONVERSÃO PARA TELEFONE ---
    default String mapTelefoneToString(Telefone telefone) {
        return telefone == null ? null : telefone.getTelefone();
    }
    default Telefone mapStringToTelefone(String telefone) {
        return telefone == null ? null : new Telefone(telefone);
    }


}