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

    // --- CORREÇÃO COM @Query ---
    // Para buscas complexas que não seguem o padrão, precisamos escrever a query.
    // Aqui, buscamos as viagens de um passageiro olhando através da tabela de tickets.
    @Query("SELECT t.viagem FROM TicketModel t WHERE t.passageiro.id = :passageiroId")
    List<ViagemModel> findViagensByPassageiroId(@Param("passageiroId") UUID passageiroId);

    // --- CORREÇÃO COM @Query ---
    // Exemplo de como uma query para "viagem em trânsito para ônibus" poderia ser.
    @Query("SELECT CASE WHEN COUNT(v) > 0 THEN TRUE ELSE FALSE END FROM ViagemModel v WHERE v.onibus.id = :onibusId AND v.statusViagem = 'EM_TRANSITO'")
    boolean existsViagemEmTransitoParaOnibus(@Param("onibusId") UUID onibusId);

    // --- CORREÇÃO COM @Query ---
    // Exemplo para "viagem futura não cancelada".
    @Query("SELECT CASE WHEN COUNT(v) > 0 THEN TRUE ELSE FALSE END FROM ViagemModel v WHERE v.onibus.id = :onibusId AND v.dataPartida > CURRENT_TIMESTAMP AND v.statusViagem <> 'CANCELADA'")
    boolean existsViagemFuturaNaoCanceladaParaOnibus(@Param("onibusId") UUID onibusId);

}
