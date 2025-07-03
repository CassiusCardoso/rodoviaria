package br.com.rodoviaria.spring_clean_arch.application.usecases.linha;

import br.com.rodoviaria.spring_clean_arch.application.dto.request.linha.CadastrarLinhaRequest;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.linha.LinhaResponse;
import br.com.rodoviaria.spring_clean_arch.application.mapper.linha.LinhaMapper;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Linha;
import br.com.rodoviaria.spring_clean_arch.domain.enums.Role;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.passageiro.AutorizacaoInvalidaException;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.viagem.LinhaInvalidaException;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.LinhaRepository;

import java.util.UUID;

public class CadastarLinhaUseCase {
    private final LinhaRepository linhaRepository;
    public CadastarLinhaUseCase(LinhaRepository linhaRepository) {
        this.linhaRepository = linhaRepository;
    }
    public LinhaResponse execute(CadastrarLinhaRequest request, Role usuarioRole){

        // Verificar se o usuário é ADMIN
        if(usuarioRole != Role.ADMINISTRADOR){
            throw new AutorizacaoInvalidaException("Apenas administradores podem cadastrar novas linhas.");
        }

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
        return LinhaMapper.INSTANCE.toResponse(linhaCadastrada);


    }
}
