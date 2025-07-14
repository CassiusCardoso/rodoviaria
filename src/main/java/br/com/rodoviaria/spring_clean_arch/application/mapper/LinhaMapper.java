package br.com.rodoviaria.spring_clean_arch.application.mapper;

import br.com.rodoviaria.spring_clean_arch.application.dto.response.linha.LinhaResponse;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Linha;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LinhaMapper {

    // O MapStruct vai mapear 'id', 'origem', 'destino', 'duracaoPrevistaMinutos',
    // 'ativo' e 'criadoEm' automaticamente, pois os nomes coincidem.
    LinhaResponse toResponse(Linha linha);

}
