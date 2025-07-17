package br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.jpa;

import br.com.rodoviaria.spring_clean_arch.domain.enums.StatusViagem;
import br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.model.LinhaModel;
import br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.model.ViagemModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface ViagemJpaRepository extends JpaRepository<ViagemModel, UUID> {

    // Método para a validação de sobreposição de ônibus
    @Query("SELECT COUNT(v.id) > 0 FROM ViagemModel v WHERE v.onibus.id = :onibusId AND v.statusViagem <> 'CANCELADA' AND v.dataPartida < :dataChegada AND v.dataHoraChegada > :dataPartida")
    boolean existeViagemSobrepostaParaOnibus(UUID onibusId, LocalDateTime dataPartida, LocalDateTime dataChegada);

    // Método para a validação de viagem duplicada
    boolean existsByLinhaAndDataPartida(LinhaModel linha, LocalDateTime dataPartida);

    // Método para buscar viagens por linha
    List<ViagemModel> findByLinhaId(UUID linhaId);

    // ▼▼▼ ADICIONE ESTE MÉTODO ▼▼▼
    // O nome segue a convenção para acessar campos de entidades relacionadas:
    // findBy[CampoDaViagem]And[EntidadeRelacionada][CampoDaEntidadeRelacionada]...
    List<ViagemModel> findByDataPartidaAndLinhaOrigemAndLinhaDestino(LocalDateTime dataPartida, String origem, String destino);

    // ▼▼▼ ADICIONE ESTA CONSULTA ▼▼▼
    @Query("SELECT DISTINCT v FROM ViagemModel v JOIN v.tickets t WHERE t.passageiro.id = :passageiroId")
    List<ViagemModel> findViagensByPassageiroId(@Param("passageiroId") UUID passageiroId);

    // ▼▼▼ ADICIONE ESTE MÉTODO ▼▼▼
    // Verifica se existe alguma viagem para o ônibus cujo status está na lista fornecida
    boolean existsByOnibusIdAndStatusViagemIn(UUID onibusId, Collection<StatusViagem> statuses);

    // ▼▼▼ ADICIONE ESTE MÉTODO FINAL ▼▼▼
    boolean existsByOnibusIdAndStatusViagemNotAndDataPartidaAfter(UUID onibusId, StatusViagem status, LocalDateTime data);
}


