package br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.jpa;

import br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.model.AdministradorModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdministradorJpaRepository extends JpaRepository<AdministradorModel, UUID> {
    Optional<AdministradorModel> findByEmail(String email);
}
