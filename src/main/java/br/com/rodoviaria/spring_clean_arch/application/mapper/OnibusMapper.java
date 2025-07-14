package br.com.rodoviaria.spring_clean_arch.application.mapper;

import br.com.rodoviaria.spring_clean_arch.application.dto.response.onibus.OnibusResponse;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Onibus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OnibusMapper {

    // Diz ao MapStruct: para preencher o campo 'placa' do DTO,
    // pegue o objeto 'placa' da entidade e acesse seu atributo 'valor'.
    @Mapping(source = "placa.valor", target = "placa")
    OnibusResponse toResponse(Onibus onibus);
    // O MapStruct vai mapear 'criadoEm' para 'criadoEm' automaticamente.

}
