package br.com.rodoviaria.spring_clean_arch.domain.repositories;

import br.com.rodoviaria.spring_clean_arch.domain.entities.Passageiro;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PassageiroRepository {

    // Salvar passageiro
    Passageiro salvar(Passageiro passageiro);

    // Buscar passageiro por id;
    Optional<Passageiro> buscarPassageiroPorId(UUID id);

    // Listar todos os passageiros cadastrados
    List<Passageiro> listarPassageiros();

    // Buscar por CPF
    // Suggest: Gemini 30/06 - 09:46
    Optional<Passageiro> buscarPorCpf(String cpf);

    // Buscar por email
    // Suggest: Gemini 30/06 - 09:46
    Optional<Passageiro> buscarPorEmail(String email);

    // Buscar passageiros por viagem
    // EDIT 03/07 09:00
    List<Passageiro> buscarPassageirosPorViagem(UUID viagemId);


    List<Passageiro> buscarPassageiroAtivo(Boolean ativo);
}
