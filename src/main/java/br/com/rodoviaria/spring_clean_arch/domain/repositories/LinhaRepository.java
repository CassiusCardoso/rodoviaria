package br.com.rodoviaria.spring_clean_arch.domain.repositories;

import br.com.rodoviaria.spring_clean_arch.domain.entities.Linha;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LinhaRepository {

    // Salvar uma linha
    Linha salvar(Linha linha);

    // Buscar uma linha por id
    Optional<Linha> buscarLinhaPorId(UUID id);

    // Listar todas as linhas disponíveis
    List<Linha> listarTodasLinhas();

    // Buscar linha pela origem e destino
    // Suggest: Gemini 30/06 - 09:46
    Optional<Linha> buscarPorOrigemEDestino(String origem, String destino)

}
