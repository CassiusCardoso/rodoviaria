package br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.jpa.passageiro;

import br.com.rodoviaria.spring_clean_arch.domain.enums.Role;
import br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.model.PassageiroModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PassageiroJpaRepository extends JpaRepository<PassageiroModel, UUID> {
    // 2. MÉTODOS DE BUSCA CUSTOMIZADOS
    // A mágica do Spring Data: basta declarar um método seguindo a convenção de nomenclatura,
    // e o Spring cria a query para você.

    /**
     * Busca um passageiro pelo seu endereço de e-mail.
     * O Spring entende "findByEmail" e gera a query: "SELECT * FROM passageiros WHERE email = ?"
     * @param email O e-mail a ser buscado.
     * @return um Optional contendo o PassageiroModel, se encontrado.
     */
    Optional<PassageiroModel> findByEmail(String email);

    /**
     * Busca um passageiro pelo seu CPF.
     * O Spring entende "findByCpf" e gera a query: "SELECT * FROM passageiros WHERE cpf = ?"
     * @param cpf O CPF a ser buscado.
     * @return um Optional contendo o PassageiroModel, se encontrado.
     */
    Optional<PassageiroModel> findByCpf(String cpf);

    Optional<PassageiroModel> findByAtivo(Boolean ativo);

    Optional<PassageiroModel> findByNome(String nome);

    Optional<PassageiroModel> findByRole(Role role);
    /**
     * Busca passageiros associados a uma viagem específica através dos tickets.
     * @param viagemId O ID da viagem.
     * @return Lista de PassageiroModel associados à viagem.
     */
    @Query("SELECT DISTINCT p FROM PassageiroModel p JOIN p.tickets t JOIN t.viagem v WHERE v.id = :viagemId")
    List<PassageiroModel> findByPassageiroPorViagem(UUID viagemId);
}

