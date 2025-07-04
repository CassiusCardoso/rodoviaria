package br.com.rodoviaria.spring_clean_arch.domain.repositories;

import br.com.rodoviaria.spring_clean_arch.domain.entities.Ticket;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Viagem;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TicketRepository {

    // Salva um ticket
    Ticket salvar(Ticket ticket);

    // Busca um ticket pelo id
    Optional<Ticket> buscarTicketPorId(UUID id);

    // Verifica se um assento específico está ocupado em uma viagem
    boolean assentoOcupado(Viagem viagem, int numeroAssento);

    // Lista todos os tickets de um passageiro
    List<Ticket> listarTicketsPorId(UUID passageiroId);

    // Listar tickets por viagem
    // EDIT 03/07 09:14
    List<Ticket> listarTicketsPorViagem(UUID viagemId);

}
