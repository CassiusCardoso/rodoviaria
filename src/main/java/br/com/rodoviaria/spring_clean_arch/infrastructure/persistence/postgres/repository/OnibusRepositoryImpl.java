package br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.repository;

import br.com.rodoviaria.spring_clean_arch.domain.entities.Onibus;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.OnibusRepository;
import br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.jpa.OnibusJpaRepository;
import br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.mapper.OnibusPersistenceMapper;
import org.springframework.stereotype.Component; // <--- MUDANÇA

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component // <--- MUDANÇA
public class OnibusRepositoryImpl implements OnibusRepository {
    private final OnibusJpaRepository jpaRepository;
    private final OnibusPersistenceMapper mapper;

    public OnibusRepositoryImpl(OnibusJpaRepository jpaRepository, OnibusPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Onibus salvar(Onibus onibus) {
        var model = mapper.toModel(onibus);
        var onibusSalvo = jpaRepository.save(model);
        return mapper.toDomain(onibusSalvo);
    }

    @Override
    public Optional<Onibus> buscarOnibusPorId(UUID onibusId){
        return jpaRepository.findById(onibusId).map(mapper::toDomain);
    }

    @Override
    public Optional<Onibus> buscarPelaPlaca(String placa){
        return jpaRepository.findByPlaca(placa).map(mapper::toDomain);
    }

    @Override
    public List<Onibus> listarTodosOnibus(){
        return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }
}