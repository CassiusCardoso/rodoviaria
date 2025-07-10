package br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.repository;

import br.com.rodoviaria.spring_clean_arch.domain.entities.Passageiro;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.PassageiroRepository;
import br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.mapper.PassageiroPersistenceMapper;
import br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.model.PassageiroModel;
import br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.jpa.PassageiroJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

// A anotação @Repository diz ao Spring que esta é a implementação de um repositório.

@Repository
public class PassageiroRepositoryImpl implements PassageiroRepository {
    private final PassageiroJpaRepository jpaPassageiroRepository; // Interface do spring
    private final PassageiroPersistenceMapper mapper;
    public PassageiroRepositoryImpl(PassageiroJpaRepository jpaPassageiroRepository, PassageiroPersistenceMapper mapper ) {
        this.jpaPassageiroRepository = jpaPassageiroRepository;
        this.mapper = mapper;
    }

    @Override
    public Passageiro salvar(Passageiro passageiro){
        PassageiroModel model = mapper.toModel(passageiro);
        PassageiroModel passageiroSalvo = jpaPassageiroRepository.save(model);
        return mapper.toDomain(passageiroSalvo); // Retornando um passageiro domínio
    }

    @Override
    public Optional<Passageiro> buscarPassageiroPorId(UUID passageiroId){
        return jpaPassageiroRepository.findById(passageiroId).map(mapper::toDomain);
    }

    @Override
    public List<Passageiro> listarPassageiros(){
        return jpaPassageiroRepository.findAll().stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public Optional<Passageiro> buscarPorCpf(String cpf){
        return jpaPassageiroRepository.findByCpf(cpf).map(mapper::toDomain);
    }

    @Override
    public Optional<Passageiro> buscarPorEmail(String email){
        return jpaPassageiroRepository.findByEmail(email).map(mapper::toDomain);
    }

    @Override
    public List<Passageiro> buscarPassageirosPorViagem(UUID viagemId){
        return jpaPassageiroRepository.findByPassageiroPorViagem(viagemId).stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public Optional<Passageiro> buscarPassageiroAtivo(Boolean ativo){
        return jpaPassageiroRepository.findByAtivo(ativo).map(mapper::toDomain);
    }

}
