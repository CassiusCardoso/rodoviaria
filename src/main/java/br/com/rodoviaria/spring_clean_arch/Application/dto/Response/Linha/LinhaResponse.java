package br.com.rodoviaria.spring_clean_arch.Application.dto.Response.Linha;

import java.util.UUID;

public record LinhaResponse (
        UUID id,
        String origem,
        String destino,
        int duracaoPrevistaMinutos
){
}
