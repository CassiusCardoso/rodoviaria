package br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.jpa;

import br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.model.TicketModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TicketJpaRepository extends JpaRepository<TicketModel, UUID> {
    // Removido findByTicketId e vou usar findById do próprio Jpa
    List<TicketModel> findByPassageiroId(UUID passageiroId);
    List<TicketModel> findByViagemId(UUID viagemId);
    // Verifica se um assento está ocupado em uma determinada viagem
    boolean existsByViagemIdAndNumeroAssento(UUID viagemId, int numeroAssento);
}
