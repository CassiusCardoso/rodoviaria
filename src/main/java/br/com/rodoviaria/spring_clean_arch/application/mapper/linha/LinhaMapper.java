package br.com.rodoviaria.spring_clean_arch.application.mapper.linha;

import br.com.rodoviaria.spring_clean_arch.application.dto.response.linha.LinhaResponse;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.viagem.ViagemResponse;
import br.com.rodoviaria.spring_clean_arch.application.mapper.onibus.OnibusMapper;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Linha;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Viagem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper()
public interface LinhaMapper {

    // O MapStruct vai mapear 'id', 'origem', 'destino', 'duracaoPrevistaMinutos',
    // 'ativo' e 'criadoEm' automaticamente, pois os nomes coincidem.
    LinhaMapper INSTANCE = Mappers.getMapper(LinhaMapper.class);
    LinhaResponse toResponse(Linha linha);

}
