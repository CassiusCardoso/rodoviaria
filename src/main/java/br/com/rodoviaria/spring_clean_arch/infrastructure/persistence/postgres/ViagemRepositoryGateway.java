package br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres;

import br.com.rodoviaria.spring_clean_arch.application.dto.response.viagem.ViagemResponse;
import br.com.rodoviaria.spring_clean_arch.application.gateway.ConsultarViagemGateway;
import br.com.rodoviaria.spring_clean_arch.application.mapper.ViagemMapper;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Viagem;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.ViagemRepository;
import br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.mapper.ViagemPersistenceMapper;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ViagemRepositoryGateway implements ConsultarViagemGateway {

    private final ViagemRepository viagemRepository;
    private final ViagemPersistenceMapper viagemPersistenceMapper;
    private final ViagemMapper viagemMapper; // <-- 2. INJETE O VIAGEMMAPPER

    public ViagemRepositoryGateway(
            ViagemRepository viagemRepository,
            ViagemPersistenceMapper viagemPersistenceMapper,
            ViagemMapper viagemMapper // <-- 3. RECEBA NO CONSTRUTOR
    ) {
        this.viagemRepository = viagemRepository;
        this.viagemPersistenceMapper = viagemPersistenceMapper;
        this.viagemMapper = viagemMapper; // <-- 4. ATRIBUA
    }

    @Override
    public ViagemResponse consultar(UUID idViagem) {
        // 4. CHAME O SEU MÉTODO PERSONALIZADO 'buscarViagemPorId'
        Viagem viagemDomain = viagemRepository.buscarViagemPorId(idViagem)
                .orElseThrow(() -> new RuntimeException("Viagem não encontrada com o ID: " + idViagem));

        // 5. AGORA O FLUXO ESTÁ CORRETO: VOCÊ JÁ TEM O OBJETO DE DOMÍNIO
        // Basta usar o ViagemMapper para convertê-lo para a resposta (DTO)
        return viagemMapper.toResponse(viagemDomain);
    }
}