package br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.jpa;

import br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.model.LinhaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface LinhaJpaRepository extends JpaRepository<LinhaModel, UUID> {

    Optional<LinhaModel> findByOrigemAndDestino(String origem, String destino);
}
