package br.com.rodoviaria.spring_clean_arch.application.usecases.viagem;

import br.com.rodoviaria.spring_clean_arch.application.dto.response.viagem.ViagemPorPassageiroResponse;
import br.com.rodoviaria.spring_clean_arch.application.mapper.ViagemMapper;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Viagem;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.passageiro.PassageiroInvalidoException;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.PassageiroRepository;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.ViagemRepository;

import java.util.List;
import java.util.UUID;

public class ListarViagensPorPassageiro {

    private final ViagemRepository viagemRepository;
    private final PassageiroRepository passageiroRepository;

    public ListarViagensPorPassageiro(ViagemRepository viagemRepository, PassageiroRepository passageiroRepository) {
        this.viagemRepository = viagemRepository;
        this.passageiroRepository = passageiroRepository;
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
                .map(ViagemMapper.INSTANCE::toViagemPorPassageiroResponse) // <<-- Usamos o novo método aqui!)
                .toList();
    }
}
