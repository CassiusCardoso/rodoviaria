package br.com.rodoviaria.spring_clean_arch.application.usecases.linha;

import br.com.rodoviaria.spring_clean_arch.application.dto.request.linha.AtualizarInformacoesDaLinhaRequest;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.linha.LinhaResponse;
import br.com.rodoviaria.spring_clean_arch.application.mapper.LinhaMapper;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Linha;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.passageiro.AutorizacaoInvalidaException;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.viagem.LinhaInvalidaException;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.LinhaRepository;

import java.util.UUID;

public class AtualizarInformacoesDaLinhaUseCase {
    private final LinhaRepository linhaRepository;
    private final LinhaMapper linhaMapper; // EDIT 11/07 15:05 Mapper adicionado para melhorar o desacomplamento
    public AtualizarInformacoesDaLinhaUseCase(LinhaRepository linhaRepository, LinhaMapper linhaMapper) {
        this.linhaRepository = linhaRepository;
        this.linhaMapper = linhaMapper;
    }
    public LinhaResponse execute(UUID linhaId, AtualizarInformacoesDaLinhaRequest request){

        // Pegando a linha específica
        Linha linhaValida = linhaRepository.buscarLinhaPorId(linhaId)
                .orElseThrow(() -> new LinhaInvalidaException("Não existe nenhuma linha registrada com o identificador" + linhaId));

        // Atualizando a entidade, como ela é imutável, precisar criar outra instância
        Linha linhaAtualizada = new Linha(
                linhaValida.getId(),
                request.origem(),
                request.destino(),
                request.duracaoPrevistaMinutos(),
                linhaValida.getAtivo()
        );

        Linha linhaSalva = linhaRepository.salvar(linhaAtualizada);

        return linhaMapper.toResponse(linhaSalva);

    }
}
