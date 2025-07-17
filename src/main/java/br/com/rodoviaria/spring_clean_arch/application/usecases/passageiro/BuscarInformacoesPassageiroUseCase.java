package br.com.rodoviaria.spring_clean_arch.application.usecases.passageiro;

import br.com.rodoviaria.spring_clean_arch.application.dto.response.passageiro.PassageiroResponse;
import br.com.rodoviaria.spring_clean_arch.application.mapper.PassageiroMapper;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Passageiro;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.passageiro.PassageiroInvalidoException;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.PassageiroRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BuscarInformacoesPassageiroUseCase {
    private final PassageiroRepository passageiroRepository;
    private final PassageiroMapper passageiroMapper; // 1. Adicione o mapper como dependência

    // 2. Injete o mapper no construtor
    public BuscarInformacoesPassageiroUseCase(PassageiroRepository passageiroRepository, PassageiroMapper passageiroMapper) {
        this.passageiroRepository = passageiroRepository;
        this.passageiroMapper = passageiroMapper;
    }


    public PassageiroResponse execute (UUID usuarioLogadoId){
        // Verificando se o passageiro existe
        Passageiro passageiro = passageiroRepository.buscarPassageiroPorId(usuarioLogadoId)
                .orElseThrow(() -> new PassageiroInvalidoException("Passageiro com identificador" + usuarioLogadoId + " não existe no sistema."));

        // 2. NÃO HÁ VALIDAÇÃO DE AUTORIZAÇÃO NECESSÁRIA AQUI
        // Porque a própria busca já foi feita com o ID do usuário correto.
        // É impossível que a variável 'passageiro' contenha dados de outro usuário.

        // 3. Mapeia para o DTO de resposta e retorna
        return passageiroMapper.toResponse(passageiro);
    }
}
