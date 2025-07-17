package br.com.rodoviaria.spring_clean_arch.application.usecases.viagem;

import br.com.rodoviaria.spring_clean_arch.application.dto.response.viagem.ViagemPorPassageiroResponse;
import br.com.rodoviaria.spring_clean_arch.application.mapper.ViagemMapper;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Viagem;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.passageiro.PassageiroInvalidoException;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.PassageiroRepository;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.ViagemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ListarViagensPorPassageiroUseCase {

    private final ViagemRepository viagemRepository;
    private final PassageiroRepository passageiroRepository;
    private final ViagemMapper viagemMapper; // EDIT 11/07 15:05 Mapper injetado

    public ListarViagensPorPassageiroUseCase(ViagemRepository viagemRepository, PassageiroRepository passageiroRepository, ViagemMapper viagemMapper) {
        this.viagemRepository = viagemRepository;
        this.passageiroRepository = passageiroRepository;
        this.viagemMapper = viagemMapper;
    }

    public List<ViagemPorPassageiroResponse> execute(UUID passageiroID){
        // Verificar se o passageiro existe
        passageiroRepository.buscarPassageiroPorId(passageiroID)
                .orElseThrow(() -> new PassageiroInvalidoException("Passageiro com identificador" + passageiroID + " não está cadastrado!"));

        List<Viagem> viagemPorPassageiro = viagemRepository.buscarViagensPorPassageiro(passageiroID);
        // 2. Validação (Opcional, mas boa prática):
        // Se a lista estiver vazia, você pode optar por retornar uma lista vazia
        // ou lançar uma exceção, dependendo da sua regra de negócio.
        // Lançar exceção pode ser mais informativo para o usuário.

        // Se a lista for vazia, o stream vai retornar uma lista vazia, o que é o comportamento correto.

        return viagemPorPassageiro.stream()
                .map(viagemMapper::toViagemPorPassageiroResponse) // <<-- Usamos o novo método aqui!)
                .toList();
    }
}
