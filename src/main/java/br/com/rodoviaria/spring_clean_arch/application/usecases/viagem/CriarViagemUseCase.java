package br.com.rodoviaria.spring_clean_arch.application.usecases.viagem;

import br.com.rodoviaria.spring_clean_arch.application.dto.request.viagem.ViagemRequest;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.viagem.ViagemResponse;
import br.com.rodoviaria.spring_clean_arch.application.mapper.ViagemMapper;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Linha;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Onibus;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Viagem;
import br.com.rodoviaria.spring_clean_arch.domain.enums.StatusViagem;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.onibus.OnibusInvalidoException;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.viagem.ViagemInvalidaException;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.LinhaRepository;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.OnibusRepository;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.ViagemRepository;

import java.time.LocalDateTime;
import java.util.UUID;

public class CriarViagemUseCase {

    private final ViagemRepository viagemRepository;
    private final LinhaRepository linhaRepository;
    private final OnibusRepository onibusRepository;

    public CriarViagemUseCase(ViagemRepository viagemRepository,  LinhaRepository linhaRepository, OnibusRepository onibusRepository) {
        this.viagemRepository = viagemRepository;
        this.linhaRepository = linhaRepository;
        this.onibusRepository = onibusRepository;
    }
    public ViagemResponse execute(ViagemRequest request) {
        // Buscar uma entidade viagem
        Linha linha = linhaRepository.buscarLinhaPorId(request.linhaId())
                .orElseThrow(() -> new ViagemInvalidaException("A viagem com a linha" + request.linhaId() + " não foi encontrada."));

        // Buscar uma entidade ônibus
        Onibus onibus = onibusRepository.buscarOnibusPorId(request.onibusId())
                .orElseThrow(() -> new OnibusInvalidoException("Ônibus com ID" + request.onibusId() + " não existe."));

        // Calcular dados gerados pelo sistema (como a data prevista para chegada)
        LocalDateTime dataChegada = request.dataPartida().plusMinutes(linha.getDuracaoPrevistaMinutos());

        // 4. CRIAR A NOVA ENTIDADE DE DOMÍNIO
        Viagem viagem = new Viagem(
                UUID.randomUUID(),
                request.dataPartida(),
                dataChegada,
                StatusViagem.AGENDADA, // EDITADO 07:27 10/07 - ESTAVA CONCLUIDA.
                linha,
                onibus
        );

        // Persistir a viagem
        Viagem viagemSalva = viagemRepository.salvar(viagem);

        // 6. MAPEAR PARA RESPONSE E RETORNAR
        // Será necessário criar o ViagemMapper, similar ao que fizemos para Ticket
        return ViagemMapper.INSTANCE.toResponse(viagemSalva);

    }
}
