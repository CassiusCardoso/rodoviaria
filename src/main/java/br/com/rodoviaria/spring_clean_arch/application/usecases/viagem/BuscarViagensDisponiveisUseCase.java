package br.com.rodoviaria.spring_clean_arch.application.usecases.viagem;

import br.com.rodoviaria.spring_clean_arch.application.dto.request.viagem.BuscarViagensDisponiveisRequest;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.viagem.ViagensDisponiveisResponse;
import br.com.rodoviaria.spring_clean_arch.application.mapper.ViagemMapper;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Viagem;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.ViagemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BuscarViagensDisponiveisUseCase {
    private final ViagemRepository viagemRepository;
    private final ViagemMapper viagemMapper; // EDIT 11/07 15:05 Mapper adicionado para melhorar o desacomplamento
    public BuscarViagensDisponiveisUseCase(ViagemRepository viagemRepository, ViagemMapper viagemMapper) {
        this.viagemRepository = viagemRepository;
        this.viagemMapper = viagemMapper;
    }

    public List<ViagensDisponiveisResponse> execute(BuscarViagensDisponiveisRequest request){
        // Verificando se a viagem existe
        List<Viagem> viagens = viagemRepository.buscarPorDataOrigemDestino(request.data(), request.origem(), request.destino());

        /// 2. Converte a lista de entidades para a lista de DTOs de forma limpa,
        // usando o novo método do mapper.
        return viagens.stream()
                .map(viagemMapper::toViagensDisponiveisResponse)
                .toList();
    }
}
