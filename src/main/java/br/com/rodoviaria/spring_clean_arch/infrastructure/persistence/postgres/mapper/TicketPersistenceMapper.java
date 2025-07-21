package br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.mapper;

import br.com.rodoviaria.spring_clean_arch.domain.entities.Ticket;
import br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.model.TicketModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses =  {PassageiroPersistenceMapper.class, ViagemPersistenceMapper.class})
public interface TicketPersistenceMapper {
    @Mapping(target = "id", source = "id")
    TicketModel toModel(Ticket ticket);
    Ticket toDomain(TicketModel model);
}