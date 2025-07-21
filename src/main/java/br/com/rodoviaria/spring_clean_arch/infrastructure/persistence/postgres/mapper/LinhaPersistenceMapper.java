package br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.mapper;

import br.com.rodoviaria.spring_clean_arch.domain.entities.Linha;
import br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.model.LinhaModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LinhaPersistenceMapper {
    @Mapping(target = "id", source = "id")
    @Mapping(target = "viagens", ignore = true)

    LinhaModel toModel(Linha linha);
    Linha toDomain(LinhaModel linhaModel);
    
}
