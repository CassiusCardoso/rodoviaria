package br.com.rodoviaria.spring_clean_arch.application.usecases.linha;

import br.com.rodoviaria.spring_clean_arch.application.dto.request.linha.AtualizarInformacoesDaLinhaRequest;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.linha.LinhaResponse;
import br.com.rodoviaria.spring_clean_arch.application.mapper.linha.LinhaMapper;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Linha;
import br.com.rodoviaria.spring_clean_arch.domain.enums.Role;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.passageiro.AutorizacaoInvalidaException;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.viagem.LinhaInvalidaException;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.LinhaRepository;

import java.util.UUID;

public class AtualizarInformacoesDaLinhaUseCase {
    private final LinhaRepository linhaRepository;
    public AtualizarInformacoesDaLinhaUseCase(LinhaRepository linhaRepository) {
        this.linhaRepository = linhaRepository;
    }
    public LinhaResponse execute(UUID linhaId, AtualizarInformacoesDaLinhaRequest request, Role usuarioRole){
        // Validando se o role é admin
        if(usuarioRole != Role.ADMINISTRADOR){
            throw new AutorizacaoInvalidaException("Somente administradores podem alterar informações de uma linha.");
        }
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

        return LinhaMapper.INSTANCE.toResponse(linhaSalva);

    }
}
