package br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.repository;

import br.com.rodoviaria.spring_clean_arch.domain.entities.Linha;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Viagem;
import br.com.rodoviaria.spring_clean_arch.domain.enums.StatusViagem;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.ViagemRepository;
import br.com.rodoviaria.spring_clean_arch.application.mapper.LinhaMapper;
import br.com.rodoviaria.spring_clean_arch.application.mapper.ViagemMapper;
import br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.jpa.ViagemJpaRepository;
import br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.mapper.ViagemPersistenceMapper;
import br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.model.LinhaModel;
import br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.model.OnibusModel;
import br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.model.ViagemModel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class ViagemRepositoryImpl implements ViagemRepository {

    private static final Logger logger = LoggerFactory.getLogger(ViagemRepositoryImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    private final ViagemJpaRepository jpaRepository;
    private final ViagemPersistenceMapper persistenceMapper;
    private final LinhaMapper linhaMapper;

    public ViagemRepositoryImpl(ViagemJpaRepository jpaRepository, ViagemPersistenceMapper persistenceMapper, LinhaMapper linhaMapper) {
        this.jpaRepository = jpaRepository;
        this.persistenceMapper = persistenceMapper;
        this.linhaMapper = linhaMapper;
    }

    @Override
    @Transactional
    public Viagem salvar(Viagem viagem) {
        logger.info("--- Iniciando método salvar para Viagem ID: {} ---", viagem.getId());

        ViagemModel viagemModel = persistenceMapper.toModel(viagem);
        if (viagemModel == null) {
            logger.error("!!! Falha crítica: viagemPersistenceMapper.toModel retornou nulo!");
            throw new IllegalStateException("viagemPersistenceMapper retornou nulo.");
        }

        logger.info("Mapeamento para ViagemModel concluído. ID da ViagemModel: {}", viagemModel.getId());

        LinhaModel linhaModel = viagemModel.getLinha();
        OnibusModel onibusModel = viagemModel.getOnibus();

        // Diagnóstico da LinhaModel
        if (linhaModel == null) {
            logger.error("!!! DIAGNÓSTICO: LinhaModel está NULA dentro da ViagemModel !!!");
        } else {
            // Supondo que LinhaModel tem um método getOrigem()
            logger.info(">>> DIAGNÓSTICO LinhaModel: ID = {}, Origem = {}", linhaModel.getId(), linhaModel.getOrigem());
            if (linhaModel.getId() == null) {
                logger.error("!!! DIAGNÓSTICO: O ID da LinhaModel está NULO !!!");
            }
        }

        // Diagnóstico do OnibusModel
        if (onibusModel == null) {
            logger.error("!!! DIAGNÓSTICO: OnibusModel está NULO dentro da ViagemModel !!!");
        } else {
            // Supondo que OnibusModel tem um método getPlaca()
            logger.info(">>> DIAGNÓSTICO OnibusModel: ID = {}, Placa = {}", onibusModel.getId(), onibusModel.getPlaca());
            if (onibusModel.getId() == null) {
                logger.error("!!! DIAGNÓSTICO: O ID do OnibusModel está NULO !!!");
            }
        }

        // Sua verificação original que causa o erro
        if (linhaModel == null || linhaModel.getId() == null) {
            logger.error("Verificação original falhou: LinhaModel é nulo ou não tem ID válido para Viagem id={}", viagem.getId());
            throw new IllegalStateException("LinhaModel não pode ser nulo ou sem ID válido");
        }
        if (onibusModel == null || onibusModel.getId() == null) {
            logger.error("Verificação original falhou: OnibusModel é nulo ou não tem ID válido para Viagem id={}", viagem.getId());
            throw new IllegalStateException("OnibusModel não pode ser nulo ou sem ID válido");
        }

        logger.info("--- Todas as verificações passaram. Salvando no banco de dados... ---");
        ViagemModel savedModel = jpaRepository.save(viagemModel);
        logger.info("--- Salvo com sucesso. Mapeando de volta para o domínio. ---");
        return persistenceMapper.toDomain(savedModel);
    }

    @Override
    public Optional<Viagem> buscarViagemPorId(UUID id) {
        Assert.notNull(id, "O id da viagem não pod ser nulo.");
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
        LinhaModel linhaModel = new LinhaModel(linha.getId(), linha.getOrigem(), linha.getDestino(), linha.getDuracaoPrevistaMinutos(), linha.getAtivo(), linha.getCriadoEm());
        return jpaRepository.existsByLinhaAndDataPartida(linhaModel, dataPartida);
    }

    @Override
    public List<Viagem> buscarPorDataOrigemDestino(LocalDateTime data, String origem, String destino) {
        return jpaRepository.findByDataPartidaAndLinhaOrigemAndLinhaDestino(data, origem, destino)
                .stream()
                .map(persistenceMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Viagem> buscarViagensPorPassageiro(UUID passageiroId) {
        return jpaRepository.findViagensByPassageiroId(passageiroId)
                .stream()
                .map(persistenceMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existeViagemEmTransitoParaOnibus(UUID onibusId) {
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