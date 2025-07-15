package br.com.rodoviaria.spring_clean_arch.infrastructure.web.controller;

import br.com.rodoviaria.spring_clean_arch.application.dto.request.viagem.BuscarViagensDisponiveisRequest;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.viagem.ViagemPorLinhaResponse;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.viagem.ViagensDisponiveisResponse;
import br.com.rodoviaria.spring_clean_arch.application.usecases.viagem.BuscarViagensDisponiveisUseCase;
import br.com.rodoviaria.spring_clean_arch.application.usecases.viagem.BuscarViagensPorLinhaUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/viagens")
@Tag(name = "Viagens", description = "Endpoints para consulta pública de viagens")
public class ViagemController {
    private final BuscarViagensDisponiveisUseCase buscarViagensDisponiveisUseCase;
    private final BuscarViagensPorLinhaUseCase buscarViagensPorLinhaUseCase;

    public ViagemController(BuscarViagensDisponiveisUseCase buscarViagensDisponiveisUseCase, BuscarViagensPorLinhaUseCase buscarViagensPorLinhaUseCase){
        this.buscarViagensDisponiveisUseCase = buscarViagensDisponiveisUseCase;
        this.buscarViagensPorLinhaUseCase = buscarViagensPorLinhaUseCase;
    }

    @GetMapping("/buscar")
    @Operation(summary = "Busca viagens disponíveis", description = "Retorna uma lista de viagens disponíveis com base na data, origem e destino.")
    public ResponseEntity<List<ViagensDisponiveisResponse>> buscarViagensDisponiveis(
            @Parameter(description = "Data e hora de partida desejada", example = "2025-12-25T08:00:00") @RequestParam LocalDateTime data,
            @Parameter(description = "Cidade de origem", example = "São Paulo - SP") @RequestParam String origem,
            @Parameter(description = "Cidade de destino", example = "Rio de Janeiro - RJ") @RequestParam String destino
    ){
        BuscarViagensDisponiveisRequest request = new BuscarViagensDisponiveisRequest(data, origem, destino);
        List<ViagensDisponiveisResponse> response = buscarViagensDisponiveisUseCase.execute(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/linha/{linhaId}")
    @Operation(summary = "Busca viagens por linha", description = "Retorna todas as viagens agendadas para uma linha específica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Viagens retornadas com sucesso"),
            @ApiResponse(responseCode = "404", description = "Linha não encontrada", content = @Content)
    })
    public ResponseEntity<List<ViagemPorLinhaResponse>> buscarViagemPorLinha(@Parameter(description = "ID da linha") @PathVariable("linhaId") UUID linhaId){
        List<ViagemPorLinhaResponse> viagensPorLinha = buscarViagensPorLinhaUseCase.execute(linhaId);
        return ResponseEntity.ok(viagensPorLinha);
    }
}