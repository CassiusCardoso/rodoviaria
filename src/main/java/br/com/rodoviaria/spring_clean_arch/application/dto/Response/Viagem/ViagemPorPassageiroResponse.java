package br.com.rodoviaria.spring_clean_arch.application.dto.response.viagem;

import br.com.rodoviaria.spring_clean_arch.application.dto.response.linha.LinhaResponse;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.onibus.OnibusResponse;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.passageiro.PassageiroResponse;

public record ViagemPorPassageiroResponse(
    LinhaResponse linha,
    PassageiroResponse passageiro,
    OnibusResponse onibus
){}
