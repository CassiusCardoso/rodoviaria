package br.com.rodoviaria.spring_clean_arch.infrastructure.web.controller;

import br.com.rodoviaria.spring_clean_arch.application.dto.request.viagem.BuscarViagensDisponiveisRequest;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.viagem.ViagemPorLinhaResponse;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.viagem.ViagensDisponiveisResponse;
import br.com.rodoviaria.spring_clean_arch.application.usecases.viagem.BuscarViagensDisponiveisUseCase;
import br.com.rodoviaria.spring_clean_arch.application.usecases.viagem.BuscarViagensPorLinhaUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/viagens")
public class ViagemController {
    private final BuscarViagensDisponiveisUseCase buscarViagensDisponiveisUseCase;
    private final BuscarViagensPorLinhaUseCase buscarViagensPorLinhaUseCase;

    public ViagemController(BuscarViagensDisponiveisUseCase buscarViagensDisponiveisUseCase, BuscarViagensPorLinhaUseCase buscarViagensPorLinhaUseCase){
        this.buscarViagensDisponiveisUseCase = buscarViagensDisponiveisUseCase;
        this.buscarViagensPorLinhaUseCase = buscarViagensPorLinhaUseCase;
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<ViagensDisponiveisResponse>> buscarViagensDisponiveis(@RequestParam LocalDateTime data, @RequestParam String origem, @RequestParam String destino){
        // 1. Crie o objeto de requisição com os parâmetros recebidos.
        BuscarViagensDisponiveisRequest request = new BuscarViagensDisponiveisRequest(data, origem, destino);

        // 2. Passe o objeto request para o caso de uso.
        List<ViagensDisponiveisResponse> response = buscarViagensDisponiveisUseCase.execute(request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/linha/{linhaId}")
    public ResponseEntity<List<ViagemPorLinhaResponse>> buscarViagemPorLinha(@PathVariable UUID linhaid){
        List<ViagemPorLinhaResponse> viagensPorLinha = buscarViagensPorLinhaUseCase.execute(linhaid);
        return ResponseEntity.ok(viagensPorLinha);
    }

}
