package br.com.rodoviaria.spring_clean_arch.infrastructure.web.controller;

import br.com.rodoviaria.spring_clean_arch.application.dto.request.onibus.AtualizarOnibusRequest;
import br.com.rodoviaria.spring_clean_arch.application.dto.request.onibus.CadastrarOnibusRequest;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.onibus.OnibusResponse;
import br.com.rodoviaria.spring_clean_arch.application.usecases.onibus.*;
import br.com.rodoviaria.spring_clean_arch.infrastructure.security.AdminAutenticado;
import br.com.rodoviaria.spring_clean_arch.infrastructure.security.UsuarioAutenticado;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/onibus")
public class OnibusController {
    private final BuscarInformacoesOnibusUseCase buscarInformacoesOnibusUseCase;

    public OnibusController(
            BuscarInformacoesOnibusUseCase buscarInformacoesOnibusUseCase
    ){
        this.buscarInformacoesOnibusUseCase = buscarInformacoesOnibusUseCase;
    }


    // PRECISA SER ADMIN PARA LISTAR TODOS OS ÔNIBUS (PERGUNTA PARA RESPONDER AMANHÃ DIA 09
    // 22:41 08/07


    @GetMapping("/{id}")
    public ResponseEntity<OnibusResponse> buscar(@PathVariable UUID onibusId){
        OnibusResponse onibusResponse = buscarInformacoesOnibusUseCase.execute(onibusId);
        return ResponseEntity.ok(onibusResponse);
    }


}
