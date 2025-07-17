package br.com.rodoviaria.spring_clean_arch.infrastructure.web.handlers;

import br.com.rodoviaria.spring_clean_arch.domain.exceptions.linha.LinhaDuplicadaException;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.onibus.OnibusInvalidoException;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.viagem.LinhaInvalidaException;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.viagem.ViagemDuplicadaException;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.viagem.ViagemInvalidaException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ViagemInvalidaException.class)
    public ResponseEntity<Map<String, String>> handleViagemInvalida(ViagemInvalidaException ex) {
        Map<String, String> errorResponse = Map.of("erro", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LinhaInvalidaException.class)
    public ResponseEntity<Map<String, String>> handleLinhaInvalida(LinhaInvalidaException ex) {
        Map<String, String> errorResponse = Map.of("erro", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LinhaDuplicadaException.class)
    public ResponseEntity<Map<String, String>> handleLinhaDuplicada(LinhaDuplicadaException ex) {
        Map<String, String> errorResponse = Map.of("erro", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(OnibusInvalidoException.class)
    public ResponseEntity<Map<String, String>> handleOnibusInvalido(OnibusInvalidoException ex) {
        Map<String, String> errorResponse = Map.of("erro", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ViagemDuplicadaException.class)
    public ResponseEntity<Map<String, String>> handleViagemDuplicada(ViagemDuplicadaException ex) {
        Map<String, String> errorResponse = Map.of("erro", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String mensagemErro = "Erro de integridade no banco de dados.";
        if (ex.getRootCause() != null && ex.getRootCause().getMessage().contains("ukput2hhrud0cqme53jt65ag2e7")) {
            mensagemErro = "Já existe uma linha com a origem e destino especificados.";
        } else if (ex.getRootCause() != null) {
            mensagemErro = ex.getRootCause().getMessage();
        }
        Map<String, String> errorResponse = Map.of("erro", mensagemErro);
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
}