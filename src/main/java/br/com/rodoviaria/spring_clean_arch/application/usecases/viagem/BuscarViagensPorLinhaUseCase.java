package br.com.rodoviaria.spring_clean_arch.application.usecases.viagem;

import br.com.rodoviaria.spring_clean_arch.application.dto.response.viagem.ViagemPorLinhaResponse;
import br.com.rodoviaria.spring_clean_arch.application.mapper.ViagemMapper;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Viagem;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.viagem.LinhaInvalidaException;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.LinhaRepository;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.ViagemRepository;

import java.util.List;
import java.util.UUID;

public class BuscarViagensPorLinhaUseCase {
    private final ViagemRepository viagemRepository;
    private final LinhaRepository linhaRepository;
    private final ViagemMapper viagemMapper; // EDIT 11/07 15:05 Mapper adicionado para melhorar o desacomplamento

    public BuscarViagensPorLinhaUseCase(ViagemRepository viagemRepository, LinhaRepository linhaRepository, ViagemMapper viagemMapper) {
        this.viagemRepository = viagemRepository;
        this.linhaRepository = linhaRepository;
        this.viagemMapper = viagemMapper;
    }

    public List<ViagemPorLinhaResponse> execute(UUID linhaId) {
        // 1. Validação da existência da Linha (melhor prática)
        linhaRepository.buscarLinhaPorId(linhaId)
                .orElseThrow(() -> new LinhaInvalidaException("A linha com ID " + linhaId + " não existe."));

        // Buscando as viagens no repositório com base no linhaId fornecido pelo usuário
        List<Viagem> viagensPorLinha = viagemRepository.buscarViagensPorLinha(linhaId);
        // 2. Validação (Opcional, mas boa prática):
        // Se a lista estiver vazia, você pode optar por retornar uma lista vazia
        // ou lançar uma exceção, dependendo da sua regra de negócio.
        // Lançar exceção pode ser mais informativo para o usuário.

        // Se a lista for vazia, o stream vai retornar uma lista vazia, o que é o comportamento correto.


        // Convertendo a lista de viagens para uma lista de ViagemPorLinhaResponse
        return viagensPorLinha.stream().map(viagemMapper::toViagemPorLinhaResponse) // <<-- Usamos o novo método aqui!
                .toList();

    }
}
