package br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.mapper;

import br.com.rodoviaria.spring_clean_arch.domain.entities.Ticket;
import br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.model.TicketModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {PassageiroPersistenceMapper.class, ViagemPersistenceMapper.class})
public interface TicketPersistenceMapper {
    TicketModel toModel(Ticket ticket);
    Ticket toDomain(TicketModel model);
}