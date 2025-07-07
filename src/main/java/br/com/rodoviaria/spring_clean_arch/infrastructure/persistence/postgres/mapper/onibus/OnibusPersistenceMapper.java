package br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.mapper.onibus;

import br.com.rodoviaria.spring_clean_arch.domain.entities.Onibus;
import br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.model.OnibusModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OnibusPersistenceMapper {
    OnibusPersistenceMapper INSTANCE = Mappers.getMapperClass(OnibusPersistenceMapper.class);
    OnibusModel toModel(Onibus onibus);
    Onibus toDomain(OnibusModel onibusModel);
}
