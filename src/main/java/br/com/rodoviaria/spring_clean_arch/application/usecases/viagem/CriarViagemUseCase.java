package br.com.rodoviaria.spring_clean_arch.application.usecases.viagem;

import br.com.rodoviaria.spring_clean_arch.application.dto.request.viagem.ViagemRequest;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.viagem.ViagemResponse;
import br.com.rodoviaria.spring_clean_arch.application.mapper.ViagemMapper;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Linha;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Onibus;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Viagem;
import br.com.rodoviaria.spring_clean_arch.domain.enums.StatusViagem;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.onibus.OnibusInvalidoException;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.viagem.ViagemDuplicadaException;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.viagem.ViagemInvalidaException;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.LinhaRepository;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.OnibusRepository;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.ViagemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class CriarViagemUseCase {

    private static final Logger logger = LoggerFactory.getLogger(CriarViagemUseCase.class);

    private final ViagemRepository viagemRepository;
    private final LinhaRepository linhaRepository;
    private final OnibusRepository onibusRepository;
    private final ViagemMapper viagemMapper;

    public CriarViagemUseCase(ViagemRepository viagemRepository, LinhaRepository linhaRepository,
                              OnibusRepository onibusRepository, ViagemMapper viagemMapper) {
        this.viagemRepository = viagemRepository;
        this.linhaRepository = linhaRepository;
        this.onibusRepository = onibusRepository;
        this.viagemMapper = viagemMapper;
    }

    public ViagemResponse execute(ViagemRequest request) {
        logger.info("Criando viagem com linhaId={}, onibusId={}, dataPartida={}",
                request.linhaId(), request.onibusId(), request.dataPartida());

        // 1. Buscar a linha pelo linhaId
        Linha linha = linhaRepository.buscarLinhaPorId(request.linhaId())
                .orElseThrow(() -> {
                    logger.error("Linha não encontrada para linhaId={}", request.linhaId());
                    return new ViagemInvalidaException("A linha com ID " + request.linhaId() + " não foi encontrada.");
                });

        logger.info("Linha encontrada: origem={}, destino={}", linha.getOrigem(), linha.getDestino());

        // 2. Buscar o ônibus pelo onibusId
        Onibus onibus = onibusRepository.buscarOnibusPorId(request.onibusId())
                .orElseThrow(() -> {
                    logger.error("Ônibus não encontrado para onibusId={}", request.onibusId());
                    return new OnibusInvalidoException("Ônibus com ID " + request.onibusId() + " não existe.");
                });

        // 3. Calcular a data de chegada
        LocalDateTime dataChegada = request.dataPartida().plusMinutes(linha.getDuracaoPrevistaMinutos());

        // 4. Verificar se existe uma viagem sobreposta para o ônibus
        if (viagemRepository.existeViagemSobrepostaParaOnibus(onibus.getId(), request.dataPartida(), dataChegada)) {
            logger.warn("Conflito de horário para ônibus com id={}", onibus.getId());
            throw new OnibusInvalidoException(
                    "Este ônibus já está alocado para outra viagem em um horário conflitante.");
        }

        // 5. Verificar se já existe uma viagem para a mesma linha e data de partida
        if (viagemRepository.existsByLinhaAndDataPartida(linha, request.dataPartida())) {
            logger.warn("Viagem duplicada para linhaId={} e dataPartida={}", linha.getId(), request.dataPartida());
            throw new ViagemDuplicadaException(
                    "Já existe uma viagem agendada para esta linha e data de partida.");
        }

        // 6. Criar a entidade de domínio Viagem
        Viagem viagem = new Viagem(
                UUID.randomUUID(),
                request.dataPartida(),
                dataChegada,
                StatusViagem.AGENDADA,
                linha,
                onibus
        );

        // 7. Persistir a viagem
        logger.info("Salvando viagem com id={}", viagem.getId());
        Viagem viagemSalva = viagemRepository.salvar(viagem);
        logger.info("Viagem salva com sucesso: id={}", viagemSalva.getId());

        // 8. Mapear para a resposta
        return viagemMapper.toResponse(viagemSalva);
    }
}