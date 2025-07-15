package br.com.rodoviaria.spring_clean_arch.infrastructure.web.controller;

import br.com.rodoviaria.spring_clean_arch.application.dto.response.linha.LinhaResponse;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.viagem.ViagemResponse;
import br.com.rodoviaria.spring_clean_arch.application.usecases.linha.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/linhas")
@Tag(name = "Linhas e Rotas", description = "Endpoints para consulta pública de linhas e viagens associadas")
public class LinhaController {
    private final BuscarViagensPorRotaUseCase buscarInformacoesDaLinhaUseCase;
    private final BuscarLinhaPorOrigemDestinoUseCase buscarLinhaPorOrigemDestinoUseCase;
    private final ListarTodasLinhasUseCase listarTodasLinhasUseCase;
    private final BuscarLinhaPorIdUseCase buscarLinhaPorIdUseCase;

    public LinhaController(
            BuscarViagensPorRotaUseCase buscarInformacoesDaLinhaUseCase,
            BuscarLinhaPorOrigemDestinoUseCase buscarLinhaPorOrigemDestinoUseCase,
            ListarTodasLinhasUseCase listarTodasLinhasUseCase,
            BuscarLinhaPorIdUseCase buscarLinhaPorIdUseCase
    ){
        this.buscarInformacoesDaLinhaUseCase = buscarInformacoesDaLinhaUseCase;
        this.buscarLinhaPorOrigemDestinoUseCase = buscarLinhaPorOrigemDestinoUseCase;
        this.listarTodasLinhasUseCase = listarTodasLinhasUseCase;
        this.buscarLinhaPorIdUseCase = buscarLinhaPorIdUseCase;
    }

    @GetMapping
    @Operation(summary = "Lista todas as linhas disponíveis", description = "Retorna uma lista com todas as linhas ativas cadastradas no sistema.")
    public ResponseEntity<List<LinhaResponse>> listarTodasLinhas(){
        List<LinhaResponse> linhas = listarTodasLinhasUseCase.execute();
        return ResponseEntity.ok(linhas);
    }

    @GetMapping("/{linhaId}")
    @Operation(summary = "Busca uma linha por ID", description = "Retorna os detalhes de uma linha específica pelo seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Linha encontrada", content = @Content(schema = @Schema(implementation = LinhaResponse.class))),
            @ApiResponse(responseCode = "404", description = "Linha não encontrada", content = @Content)
    })
    public ResponseEntity<LinhaResponse> buscarPorId(@Parameter(description = "ID da linha a ser buscada") @PathVariable UUID linhaId) {
        LinhaResponse linhaResponse = buscarLinhaPorIdUseCase.execute(linhaId);
        return ResponseEntity.ok(linhaResponse);
    }

    @GetMapping("/{linhaId}/viagens")
    @Operation(summary = "Lista as viagens de uma linha", description = "Retorna todas as viagens agendadas para uma linha específica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Viagens retornadas com sucesso"),
            @ApiResponse(responseCode = "404", description = "Linha não encontrada", content = @Content)
    })
    public ResponseEntity<List<ViagemResponse>> listarViagensDaLinha(@Parameter(description = "ID da linha para listar as viagens") @PathVariable UUID linhaId){
        List<ViagemResponse> viagens = buscarInformacoesDaLinhaUseCase.execute(linhaId);
        return ResponseEntity.ok(viagens);
    }

    @GetMapping("/buscar")
    @Operation(summary = "Busca uma linha por origem e destino", description = "Permite encontrar uma linha específica informando as cidades de origem e destino.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Linha encontrada", content = @Content(schema = @Schema(implementation = LinhaResponse.class))),
            @ApiResponse(responseCode = "404", description = "Nenhuma linha encontrada para a rota informada", content = @Content)
    })
    public ResponseEntity<LinhaResponse> buscarPorRota(@Parameter(description = "Cidade de origem", example = "São Paulo - SP") @RequestParam String origem, @Parameter(description = "Cidade de destino", example = "Rio de Janeiro - RJ") @RequestParam String destino){
        LinhaResponse linhaResponse = buscarLinhaPorOrigemDestinoUseCase.execute(origem, destino);
        return ResponseEntity.ok(linhaResponse);
    }
}