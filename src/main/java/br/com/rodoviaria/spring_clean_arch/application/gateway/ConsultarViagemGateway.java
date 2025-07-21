package br.com.rodoviaria.spring_clean_arch.application.gateway;

import br.com.rodoviaria.spring_clean_arch.application.dto.response.viagem.ViagemResponse;

import java.util.UUID;

public interface ConsultarViagemGateway {
    ViagemResponse consultar(UUID idViagem);
}
