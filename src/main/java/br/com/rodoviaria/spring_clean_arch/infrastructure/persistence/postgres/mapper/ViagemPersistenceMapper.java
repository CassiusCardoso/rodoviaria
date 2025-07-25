package br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.mapper;

import br.com.rodoviaria.spring_clean_arch.domain.entities.Viagem;
import br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.model.ViagemModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {LinhaPersistenceMapper.class, OnibusPersistenceMapper.class})
public interface ViagemPersistenceMapper {
    @Mapping(target = "id", source = "id")
    ViagemModel toModel(Viagem viagem);
    Viagem toDomain(ViagemModel viagemModel);

}
