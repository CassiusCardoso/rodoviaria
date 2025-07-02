package br.com.rodoviaria.spring_clean_arch.domain.repositories;

import br.com.rodoviaria.spring_clean_arch.domain.entities.Viagem;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ViagemRepository {

    // Salvar uma viagem
    Viagem salvar(Viagem viagem);

    // Buscar viagem por id
    Optional<Viagem> buscarViagemPorId(UUID id);

    // Listar todas as viagens de um passageiro
    List<Viagem> listarTicketsPorId(UUID viagemId);

    // Buscar pela data, origem e destino
    // Suggest: Gemini 30/06 - 09:46
    List<Viagem> buscarPorDataOrigemDestino(LocalDate data, String origem, String destino);
}
