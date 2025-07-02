package br.com.rodoviaria.spring_clean_arch.application.mapper.passageiro;

import br.com.rodoviaria.spring_clean_arch.application.dto.response.passageiro.PassageiroResponse;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Passageiro;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PassageiroMapper {

    PassageiroMapper INSTANCE = Mappers.getMapper(PassageiroMapper.class);

    @Mapping(source = "email.email", target = "email")
    @Mapping(source = "cpf.cpf", target = "cpf")
    @Mapping(source = "telefone.telefone", target = "telefone")
    PassageiroResponse toResponse(Passageiro passageiro);
}
