package br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.jpa;

import br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.model.PassageiroModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PassageiroJpaRepository extends JpaRepository<PassageiroModel, UUID> {

    Optional<PassageiroModel> findByEmail(String email);
    Optional<PassageiroModel> findByCpf(String cpf);

    // ▼▼▼ ALTERADO PARA LISTA ▼▼▼
    List<PassageiroModel> findByAtivo(Boolean ativo);

    // ▼▼▼ ALTERADO PARA LISTA ▼▼▼
    List<PassageiroModel> findByNome(String nome);

    @Query("SELECT DISTINCT p FROM PassageiroModel p JOIN p.tickets t JOIN t.viagem v WHERE v.id = :viagemId")
    List<PassageiroModel> findByPassageiroPorViagem(UUID viagemId);
}

