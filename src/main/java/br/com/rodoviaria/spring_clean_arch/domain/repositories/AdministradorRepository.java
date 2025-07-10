package br.com.rodoviaria.spring_clean_arch.domain.repositories;

import br.com.rodoviaria.spring_clean_arch.domain.entities.Administrador;

import java.util.Optional;
import java.util.UUID;

public interface AdministradorRepository {
    Administrador salvar(Administrador administrador);
    Optional<Administrador> buscarPorId(UUID id);

    Optional<Administrador> buscarPorEmail(String email);
}
