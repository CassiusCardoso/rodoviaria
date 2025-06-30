package br.com.rodoviaria.spring_clean_arch.domain.Repositories;

import br.com.rodoviaria.spring_clean_arch.domain.Entities.Onibus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OnibusRepository {

    // Salvar um ônibus
    Onibus salvar (Onibus onibus);

    // Listar ônibus por id
    Optional<Onibus> buscarOnibusPorId(UUID id);

    // Listar todos os ônibus disponíveis
    List<Onibus> listarTodosOnibus();

    // Buscar ônibus pela placa
    // Suggest: Gemini 30/06 - 09:46
    Optional<Onibus> buscarPelaPlaca(String placa);
}
