package br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.repository;

import br.com.rodoviaria.spring_clean_arch.domain.entities.Linha;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.LinhaRepository;
import br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.jpa.LinhaJpaRepository;
import br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.mapper.LinhaPersistenceMapper;
import org.springframework.stereotype.Component; // <--- MUDANÇA

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component // <--- MUDANÇA
public class LinhaRepositoryImpl implements LinhaRepository {
    private final LinhaJpaRepository jpaRepository;
    private final LinhaPersistenceMapper mapper;

    public LinhaRepositoryImpl(LinhaJpaRepository jpaRepository, LinhaPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Linha salvar(Linha linha){
        var model = mapper.toModel(linha);
        var linhaSalva = jpaRepository.save(model);
        return mapper.toDomain(linhaSalva);
    }

    @Override
    public Optional<Linha> buscarLinhaPorId(UUID linhaId){
        return jpaRepository.findById(linhaId).map(mapper::toDomain);
    }

    @Override
    public Optional<Linha> buscarPorOrigemEDestino(String origem, String destino){
        return jpaRepository.findByOrigemAndDestino(origem, destino).map(mapper::toDomain);
    }

    @Override
    public List<Linha> listarTodasLinhas(){
        return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }
}