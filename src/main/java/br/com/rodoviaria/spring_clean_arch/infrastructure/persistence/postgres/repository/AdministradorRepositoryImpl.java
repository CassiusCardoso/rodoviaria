package br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.repository;

import br.com.rodoviaria.spring_clean_arch.domain.entities.Administrador;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.AdministradorRepository;
import br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.jpa.AdministradorJpaRepository;
import br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.mapper.AdministradorPersistenceMapper;
import br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.model.AdministradorModel;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class AdministradorRepositoryImpl implements AdministradorRepository {
    private final AdministradorJpaRepository jpaRepository;
    private final AdministradorPersistenceMapper mapper;

    public AdministradorRepositoryImpl(AdministradorJpaRepository jpaRepository, AdministradorPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Administrador salvar(Administrador administrador){
        AdministradorModel model = mapper.toModel(administrador);
        AdministradorModel administradorSalvo = jpaRepository.save(model);
        return mapper.toDomain(administradorSalvo);
    }

    @Override
    public Optional<Administrador> buscarPorEmail(String email){
        return jpaRepository.findByEmail(email).map(mapper::toDomain);
    }

    @Override
    public Optional<Administrador> buscarPorId(UUID adminId){
        return jpaRepository.findById(adminId).map(mapper::toDomain);
    }
}
