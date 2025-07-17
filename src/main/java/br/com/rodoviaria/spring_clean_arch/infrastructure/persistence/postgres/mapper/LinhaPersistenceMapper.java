package br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.mapper;

import br.com.rodoviaria.spring_clean_arch.domain.entities.Linha;
import br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.model.LinhaModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface LinhaPersistenceMapper {
    LinhaModel toModel(Linha linha);
    Linha toDomain(LinhaModel linhaModel);
    
}
