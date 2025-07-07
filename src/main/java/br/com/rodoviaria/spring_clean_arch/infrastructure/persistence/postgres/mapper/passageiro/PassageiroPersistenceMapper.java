package br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.mapper;

import br.com.rodoviaria.spring_clean_arch.domain.entities.Passageiro;
import br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.model.PassageiroModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring") // Adicione componentModel = "spring" para o Spring injetá-lo
public interface PassageiroPersistenceMapper {
    PassageiroPersistenceMapper INSTANCE = Mappers.getMapper(PassageiroPersistenceMapper.class);

    /**
     * Converte a Entidade de Domínio (com VOs) para o Modelo de Persistência (com tipos primitivos).
     */
    @Mapping(source = "email.email", target = "email")
    @Mapping(source = "senha.senhaHash", target = "senha")
    @Mapping(source = "cpf.cpf", target = "cpf")
    @Mapping(source = "telefone.telefone", target = "telefone")
    PassageiroModel toModel(Passageiro passageiro);


    /**
     * Converte o Modelo de Persistência (com tipos primitivos) de volta para a Entidade de Domínio (com VOs).
     * O MapStruct é inteligente o suficiente para chamar os construtores dos VOs.
     */
    // Não precisamos de @Mapping aqui, pois o MapStruct tentará chamar 'new Email(model.getEmail())' etc.
    // Se isso falhar, podemos adicionar o mapeamento explícito.
    Passageiro toDomain(PassageiroModel model);
}