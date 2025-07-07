package br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.jpa.viagem;

import br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.model.ViagemModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ViagemJpaRepository extends JpaRepository<ViagemModel, UUID> {
    List<ViagemModel> findByLinhaId(UUID linhaId);
    List<ViagemModel> findByOnibusId(UUID onibusId);
    List<ViagemModel> findByDataPartidaAndLinhaOrigemAndLinhaDestino(
            LocalDateTime data, String origem, String destino);

    List<ViagemModel> findByViagensPorLinha(UUID linhaId);
    // Query para buscar viagens associadas a um passageiro através dos tickets
    @Query("SELECT t.viagem FROM TicketModel t WHERE t.passageiro.id = :passageiroId")
    List<ViagemModel> findViagensByPassageiroId(@Param("passageiroId") UUID passageiroId);
    boolean existsByViagemEmTransitoParaOnibus(UUID onibusId);
    boolean existsByViagemFuturaNaoCanceladaParaOnibus(UUID onibusId);
    List<ViagemModel> findByTicketsPorId(UUID viagemId);

}
