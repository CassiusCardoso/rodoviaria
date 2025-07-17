package br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.repository;

import br.com.rodoviaria.spring_clean_arch.domain.entities.Passageiro;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.PassageiroRepository;
import br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.mapper.PassageiroPersistenceMapper;
import br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.jpa.PassageiroJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class PassageiroRepositoryImpl implements PassageiroRepository {
    private final PassageiroJpaRepository jpaRepository;
    private final PassageiroPersistenceMapper mapper;

    public PassageiroRepositoryImpl(PassageiroJpaRepository jpaRepository, PassageiroPersistenceMapper mapper ) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Passageiro salvar(Passageiro passageiro){
        var model = mapper.toModel(passageiro);
        var passageiroSalvo = jpaRepository.save(model);
        return mapper.toDomain(passageiroSalvo);
    }

    @Override
    public Optional<Passageiro> buscarPassageiroPorId(UUID passageiroId){
        return jpaRepository.findById(passageiroId).map(mapper::toDomain);
    }

    @Override
    public List<Passageiro> listarPassageiros(){
        return jpaRepository.findAll().stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public Optional<Passageiro> buscarPorCpf(String cpf){
        return jpaRepository.findByCpf(cpf).map(mapper::toDomain);
    }

    @Override
    public Optional<Passageiro> buscarPorEmail(String email){
        return jpaRepository.findByEmail(email).map(mapper::toDomain);
    }

    @Override
    public List<Passageiro> buscarPassageirosPorViagem(UUID viagemId){
        return jpaRepository.findByPassageiroPorViagem(viagemId).stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Passageiro> buscarPassageiroAtivo(Boolean ativo){ // <--- NOME CORRIGIDO
        return jpaRepository.findByAtivo(ativo).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}