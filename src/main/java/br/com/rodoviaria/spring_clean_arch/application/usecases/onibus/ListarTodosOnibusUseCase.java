package br.com.rodoviaria.spring_clean_arch.application.usecases.onibus;

import br.com.rodoviaria.spring_clean_arch.application.dto.response.onibus.OnibusResponse;
import br.com.rodoviaria.spring_clean_arch.application.mapper.onibus.OnibusMapper;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Onibus;
import br.com.rodoviaria.spring_clean_arch.domain.enums.Role;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.passageiro.AutorizacaoInvalidaException;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.OnibusRepository;

import java.util.List;

public class ListarTodosOnibusUseCase {

    private final OnibusRepository onibusRepository;
    public ListarTodosOnibusUseCase(OnibusRepository onibusRepository) {
        this.onibusRepository = onibusRepository;
    }

    public List<OnibusResponse> execute(Role usuarioRole){

        // 1. VERIFICAÇÃO DE AUTORIZAÇÃO
        // A primeira coisa a se fazer é garantir que apenas um admin pode ver a frota.
        if (usuarioRole != Role.ADMINISTRADOR) {
            throw new AutorizacaoInvalidaException("Apenas administradores podem listar todos os ônibus.");
        }
        // Verificando se existe uma lista de ônibus
        List<Onibus> todosOsOnibus = onibusRepository.listarTodosOnibus();

        // 3. MAPEIA A LISTA DE ENTIDADES PARA UMA LISTA DE DTOS
        return todosOsOnibus.stream()
                .map(OnibusMapper.INSTANCE::toResponse)
                .toList();
    }
}
