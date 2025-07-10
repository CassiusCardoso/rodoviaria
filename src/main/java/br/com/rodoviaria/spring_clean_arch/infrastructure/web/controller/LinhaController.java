package br.com.rodoviaria.spring_clean_arch.infrastructure.web.controller;

import br.com.rodoviaria.spring_clean_arch.application.dto.response.linha.LinhaResponse;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.viagem.ViagemResponse;
import br.com.rodoviaria.spring_clean_arch.application.usecases.linha.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/linhas")
public class LinhaController {
    private final BuscarViagensPorRotaUseCase buscarInformacoesDaLinhaUseCase;
    private final BuscarLinhaPorOrigemDestinoUseCase buscarLinhaPorOrigemDestinoUseCase;
    private final ListarTodasLinhasUseCase listarTodasLinhasUseCase;
    private final BuscarLinhaPorIdUseCase buscarLinhaPorIdUseCase;

    public LinhaController(
            BuscarViagensPorRotaUseCase buscarInformacoesDaLinhaUseCase,
            BuscarLinhaPorOrigemDestinoUseCase buscarLinhaPorOrigemDestinoUseCase, // S
            ListarTodasLinhasUseCase listarTodasLinhasUseCase,
            BuscarLinhaPorIdUseCase buscarLinhaPorIdUseCase
    ){
        this.buscarInformacoesDaLinhaUseCase = buscarInformacoesDaLinhaUseCase;
        this.buscarLinhaPorOrigemDestinoUseCase = buscarLinhaPorOrigemDestinoUseCase;
        this.listarTodasLinhasUseCase = listarTodasLinhasUseCase;
        this.buscarLinhaPorIdUseCase = buscarLinhaPorIdUseCase;
    }

    @GetMapping
    public ResponseEntity<List<LinhaResponse>> listarTodasLinhas(){
        List<LinhaResponse> linhas = listarTodasLinhasUseCase.execute();
        return ResponseEntity.ok(linhas);
    }

    @GetMapping("/{linhaId}")
    public ResponseEntity<LinhaResponse> buscarPorId(@PathVariable UUID linhaId) {
        LinhaResponse linhaResponse = buscarLinhaPorIdUseCase.execute(linhaId);
        return ResponseEntity.ok(linhaResponse);
    }

    @GetMapping("/{linhaId}/viagens")
    public ResponseEntity<List<ViagemResponse>> listarViagensDaLinha(@PathVariable UUID linhaId){
        List<ViagemResponse> viagens = buscarInformacoesDaLinhaUseCase.execute(linhaId);
        return ResponseEntity.ok(viagens);
    }

    @GetMapping("/buscar")
    public ResponseEntity<LinhaResponse> buscarPorRota(@RequestParam String origem, @RequestParam String destino){
        LinhaResponse linhaResponse = buscarLinhaPorOrigemDestinoUseCase.execute(origem, destino);
        return ResponseEntity.ok(linhaResponse);
    }
}

