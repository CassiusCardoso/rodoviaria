package br.com.rodoviaria.spring_clean_arch.infrastructure.web.controller;

import br.com.rodoviaria.spring_clean_arch.application.dto.response.onibus.OnibusResponse;
import br.com.rodoviaria.spring_clean_arch.application.usecases.onibus.BuscarInformacoesOnibusUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/onibus")
@Tag(name = "Ônibus", description = "Endpoints para consulta pública de informações de ônibus")
public class OnibusController {
    private final BuscarInformacoesOnibusUseCase buscarInformacoesOnibusUseCase;

    public OnibusController(
            BuscarInformacoesOnibusUseCase buscarInformacoesOnibusUseCase
    ){
        this.buscarInformacoesOnibusUseCase = buscarInformacoesOnibusUseCase;
    }

    @GetMapping("/{onibusId}")
    @Operation(summary = "Busca um ônibus por ID", description = "Retorna os detalhes públicos de um ônibus específico. Este endpoint é de acesso público.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ônibus encontrado", content = @Content(schema = @Schema(implementation = OnibusResponse.class))),
            @ApiResponse(responseCode = "404", description = "Ônibus não encontrado", content = @Content)
    })
    public ResponseEntity<OnibusResponse> buscar(@Parameter(description = "ID do ônibus a ser buscado") @PathVariable UUID onibusId){
        OnibusResponse onibusResponse = buscarInformacoesOnibusUseCase.execute(onibusId);
        return ResponseEntity.ok(onibusResponse);
    }
}