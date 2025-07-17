package br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.repository;

import br.com.rodoviaria.spring_clean_arch.domain.entities.Linha;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Viagem;
import br.com.rodoviaria.spring_clean_arch.domain.enums.StatusViagem;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.ViagemRepository;
// ▼▼▼ IMPORTS CORRIGIDOS ▼▼▼
import br.com.rodoviaria.spring_clean_arch.application.mapper.LinhaMapper;
import br.com.rodoviaria.spring_clean_arch.application.mapper.ViagemMapper;
import br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.jpa.ViagemJpaRepository;
import br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.mapper.ViagemPersistenceMapper;
import br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.model.LinhaModel;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ViagemRepositoryImpl implements ViagemRepository {

    private final ViagemJpaRepository jpaRepository;
    private final ViagemPersistenceMapper persistenceMapper; // Usando o mapper de persistência
    private final LinhaMapper linhaMapper; // Mapper da camada de aplicação para DTOs, se necessário

    public ViagemRepositoryImpl(ViagemJpaRepository jpaRepository, ViagemPersistenceMapper persistenceMapper, LinhaMapper linhaMapper) {
        this.jpaRepository = jpaRepository;
        this.persistenceMapper = persistenceMapper;
        this.linhaMapper = linhaMapper;
    }

    @Override
    public Viagem salvar(Viagem viagem) {
        var viagemModel = persistenceMapper.toModel(viagem);
        var viagemSalva = jpaRepository.save(viagemModel);
        return persistenceMapper.toDomain(viagemSalva);
    }

    @Override
    public Optional<Viagem> buscarViagemPorId(UUID id) {
        return jpaRepository.findById(id).map(persistenceMapper::toDomain);
    }

    @Override
    public List<Viagem> buscarViagensPorLinha(UUID linhaId) {
        return jpaRepository.findByLinhaId(linhaId).stream()
                .map(persistenceMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existeViagemSobrepostaParaOnibus(UUID onibusId, LocalDateTime dataPartida, LocalDateTime dataChegada) {
        return jpaRepository.existeViagemSobrepostaParaOnibus(onibusId, dataPartida, dataChegada);
    }

    @Override
    public boolean existsByLinhaAndDataPartida(Linha linha, LocalDateTime dataPartida) {
        // Para esta checagem, precisamos converter apenas a Linha de domínio para o modelo de persistência.
        // Assumindo que você tem um `LinhaPersistenceMapper` para isso.
        // Se `LinhaMapper` já faz isso, o nome pode ser confuso. Vamos assumir um `LinhaPersistenceMapper`.
        // Se não tiver, o ideal é criar um ou usar o `LinhaMapper` se ele fizer a conversão para `LinhaModel`.
        LinhaModel linhaModel = new LinhaModel(linha.getId(), linha.getOrigem(), linha.getDestino(), linha.getDuracaoPrevistaMinutos(), linha.getAtivo(), linha.getCriadoEm());
        return jpaRepository.existsByLinhaAndDataPartida(linhaModel, dataPartida);
    }

    // ▼▼▼ IMPLEMENTAÇÃO DO MÉTODO FALTANTE ▼▼▼
    @Override
    public List<Viagem> buscarPorDataOrigemDestino(LocalDateTime data, String origem, String destino) {
        return jpaRepository.findByDataPartidaAndLinhaOrigemAndLinhaDestino(data, origem, destino)
                .stream()
                .map(persistenceMapper::toDomain)
                .collect(Collectors.toList());
    }
    // ▼▼▼ IMPLEMENTAÇÃO DO MÉTODO FINAL ▼▼▼
    @Override
    public List<Viagem> buscarViagensPorPassageiro(UUID passageiroId) {
        return jpaRepository.findViagensByPassageiroId(passageiroId)
                .stream()
                .map(persistenceMapper::toDomain)
                .collect(Collectors.toList());
    }

    // ▼▼▼ IMPLEMENTAÇÃO DO MÉTODO FINAL ▼▼▼
    @Override
    public boolean existeViagemEmTransitoParaOnibus(UUID onibusId) {
        // Define quais status não permitem que um ônibus seja desativado, por exemplo.
        // Ajuste essa lista conforme sua regra de negócio.
        List<StatusViagem> statusAtivos = List.of(StatusViagem.AGENDADA, StatusViagem.EM_TRANSITO);
        return jpaRepository.existsByOnibusIdAndStatusViagemIn(onibusId, statusAtivos);
    }

    @Override
    public boolean existeViagemFuturaNaoCanceladaParaOnibus(UUID onibusId) {
        return jpaRepository.existsByOnibusIdAndStatusViagemNotAndDataPartidaAfter(
                onibusId,
                StatusViagem.CANCELADA,
                LocalDateTime.now()
        );
    }

}