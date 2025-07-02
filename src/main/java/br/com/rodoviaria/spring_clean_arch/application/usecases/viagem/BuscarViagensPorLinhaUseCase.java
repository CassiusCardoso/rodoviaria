package br.com.rodoviaria.spring_clean_arch.application.usecases.viagem;

import br.com.rodoviaria.spring_clean_arch.application.dto.request.viagem.ViagemRequest;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.viagem.ViagemPorLinhaResponse;
import br.com.rodoviaria.spring_clean_arch.application.mapper.viagem.ViagemMapper;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Linha;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Onibus;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Viagem;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.onibus.OnibusInvalidoException;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.viagem.LinhaInvalidaException;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.viagem.ViagemInvalidaException;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.LinhaRepository;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.OnibusRepository;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.ViagemRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class BuscarViagensPorLinhaUseCase {
    private final ViagemRepository viagemRepository;
    private final LinhaRepository linhaRepository;

    public BuscarViagensPorLinhaUseCase(ViagemRepository viagemRepository, LinhaRepository linhaRepository) {
        this.viagemRepository = viagemRepository;
        this.linhaRepository = linhaRepository;
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
        return viagensPorLinha.stream().map(ViagemMapper.INSTANCE::toViagemPorLinhaResponse) // <<-- Usamos o novo método aqui!
                .toList();

    }
}
