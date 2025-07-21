package br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.repository;

import br.com.rodoviaria.spring_clean_arch.domain.entities.Ticket;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Viagem;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.TicketRepository;
import br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.mapper.TicketPersistenceMapper;
import br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.jpa.TicketJpaRepository;
import org.springframework.stereotype.Component; // <--- MUDANÇA
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class TicketRepositoryImpl implements TicketRepository {
    private final TicketJpaRepository jpaRepository;
    private final TicketPersistenceMapper mapper;

    public TicketRepositoryImpl(TicketJpaRepository jpaRepository, TicketPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Ticket salvar(Ticket ticket) {
        var model = mapper.toModel(ticket);
        var ticketSalvo = jpaRepository.save(model);
        return mapper.toDomain(ticketSalvo);
    }

    @Override
    public Optional<Ticket> buscarTicketPorId(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public boolean assentoOcupado(Viagem viagem, int numeroAssento) {
        return jpaRepository.existsByViagemIdAndNumeroAssento(viagem.getId(), numeroAssento);
    }

    @Override
    public List<Ticket> listarTicketsPorPassageiroId(UUID passageiroId) {
        return jpaRepository.findByPassageiroId(passageiroId).stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Ticket> listarTicketsPorViagem(UUID viagemId) {
        return jpaRepository.findByViagemId(viagemId).stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Ticket> listarTickets() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }
}