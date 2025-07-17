package br.com.rodoviaria.spring_clean_arch.application.usecases.linha;

import br.com.rodoviaria.spring_clean_arch.application.dto.request.linha.CadastrarLinhaRequest;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.linha.LinhaResponse;
import br.com.rodoviaria.spring_clean_arch.application.mapper.LinhaMapper;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Linha;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.linha.LinhaDuplicadaException;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.LinhaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CadastrarLinhaUseCase {
    private static final Logger logger = LoggerFactory.getLogger(CadastrarLinhaUseCase.class);

    private final LinhaRepository linhaRepository;
    private final LinhaMapper linhaMapper;

    public CadastrarLinhaUseCase(LinhaRepository linhaRepository, LinhaMapper linhaMapper) {
        this.linhaRepository = linhaRepository;
        this.linhaMapper = linhaMapper;
    }

    public LinhaResponse execute(CadastrarLinhaRequest request) {
        logger.info("Tentando cadastrar linha com origem={} e destino={}", request.origem(), request.destino());

        // Verificar se a linha já existe
        linhaRepository.buscarPorOrigemEDestino(request.origem(), request.destino())
                .ifPresent(linhaExistente -> {
                    logger.warn("Linha duplicada encontrada: origem={}, destino={}",
                            linhaExistente.getOrigem(), linhaExistente.getDestino());
                    throw new LinhaDuplicadaException(
                            "Já existe uma linha cadastrada com origem '" + request.origem() +
                                    "' e destino '" + request.destino() + "'.");
                });

        // Criar a entidade de domínio
        Linha novaLinha = new Linha(
                UUID.randomUUID(),
                request.origem(),
                request.destino(),
                request.duracaoPrevistaMinutos(),
                true
        );

        // Persistir a entidade
        Linha linhaCadastrada = linhaRepository.salvar(novaLinha);
        logger.info("Linha cadastrada com sucesso: id={}", linhaCadastrada.getId());

        // Mapear para o DTO de resposta
        return linhaMapper.toResponse(linhaCadastrada);
    }
}