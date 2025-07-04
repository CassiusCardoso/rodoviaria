package br.com.rodoviaria.spring_clean_arch.application.dto.response.linha;

import java.time.LocalDateTime;
import java.util.UUID;

public record LinhaResponse (
        UUID id,
        String origem,
        String destino,
        int duracaoPrevistaMinutos,
        Boolean ativo,           // 2. Adicione o novo campo
        LocalDateTime criadoEm   // 3. Adicione o novo campo
){
}
