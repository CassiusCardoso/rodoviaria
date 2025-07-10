package br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.repository.viagem;

import br.com.rodoviaria.spring_clean_arch.domain.entities.Viagem;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.ViagemRepository;
import br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.mapper.viagem.ViagemPersistenceMapper;
import br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.model.ViagemModel;
import br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.jpa.viagem.ViagemJpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ViagemRepositoryImpl implements ViagemRepository {
    private final ViagemJpaRepository jpaRepository;
    private final ViagemPersistenceMapper mapper;

    public ViagemRepositoryImpl(ViagemJpaRepository jpaRepository, ViagemPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }
    // implementar os métodos da interface ViagemRepository

    @Override
    public Viagem salvar(Viagem viagem) {
        ViagemModel model = mapper.toModel(viagem);
        ViagemModel viagemSalva = jpaRepository.save(model);
        return mapper.toDomain(viagemSalva);
    }

    @Override
    public Optional<Viagem> buscarViagemPorId(UUID viagemId) {
        return jpaRepository.findById(viagemId).map(mapper::toDomain);
    }

    @Override
    public List<Viagem> buscarPorDataOrigemDestino(LocalDateTime data, String origem, String destino){
        return jpaRepository.findByDataPartidaAndLinhaOrigemAndLinhaDestino(data, origem, destino).stream().map(mapper::toDomain).toList();
    }


    @Override
    public List<Viagem> buscarViagensPorPassageiro(UUID passageiroId){
        return jpaRepository.findViagensByPassageiroId(passageiroId).stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Viagem> buscarViagensPorLinha(UUID linhaId){
        return jpaRepository.findByLinhaId(linhaId).stream().map(mapper::toDomain).toList();
    }

    @Override
    public boolean existeViagemEmTransitoParaOnibus(UUID onibusId) {
        return jpaRepository.existsViagemEmTransitoParaOnibus(onibusId);
    }

    @Override
    public boolean existeViagemFuturaNaoCanceladaParaOnibus(UUID onibusId) {
        return jpaRepository.existsViagemFuturaNaoCanceladaParaOnibus(onibusId);
    }

}
