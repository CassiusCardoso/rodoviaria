package br.com.rodoviaria.spring_clean_arch.application.usecases.linha;

import br.com.rodoviaria.spring_clean_arch.application.dto.request.linha.CadastrarLinhaRequest;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.linha.LinhaResponse;
import br.com.rodoviaria.spring_clean_arch.application.mapper.LinhaMapper;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Linha;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.passageiro.AutorizacaoInvalidaException;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.viagem.LinhaInvalidaException;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.LinhaRepository;

import java.util.UUID;

public class CadastrarLinhaUseCase {
    private final LinhaRepository linhaRepository;
    private final LinhaMapper linhaMapper;

    public CadastrarLinhaUseCase(LinhaRepository linhaRepository, LinhaMapper linhaMapper) {
        this.linhaRepository = linhaRepository;
        this.linhaMapper = linhaMapper; // EDIT 11/07 15:05 Mapper adicionado para melhorar o desacomplamento
    }
    public LinhaResponse execute(CadastrarLinhaRequest request){

        // Verificar se a linha já existe
        linhaRepository.buscarPorOrigemEDestino(request.origem(), request.destino())
            .ifPresent(linhaExistente -> {
                throw new LinhaInvalidaException("Já existe uma linha cadastrada com esta mesma origem e destino.");
                });
        // Retornar uma nova linha
        Linha novaLinha = new Linha(
                UUID.randomUUID(),
                request.origem(),
                request.destino(),
                request.duracaoPrevistaMinutos(),
                true
        );

        // Persistir a entidade
        Linha linhaCadastrada = linhaRepository.salvar(novaLinha);

        // Mapear para o DTO de resposta
        return linhaMapper.toResponse(linhaCadastrada);


    }
}
