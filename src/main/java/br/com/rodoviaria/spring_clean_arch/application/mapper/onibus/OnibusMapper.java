package br.com.rodoviaria.spring_clean_arch.application.mapper.onibus;

import br.com.rodoviaria.spring_clean_arch.application.dto.response.onibus.OnibusResponse;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Onibus;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OnibusMapper {
    OnibusMapper INSTANCE = Mappers.getMapper(OnibusMapper.class);
    OnibusResponse toResponse(Onibus onibus);

}
