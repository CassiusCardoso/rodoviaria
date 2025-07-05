package br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.repository;

import br.com.rodoviaria.spring_clean_arch.application.mapper.passageiro.PassageiroMapper;
import br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.springdata.JpaPassageiroRepository;
import org.springframework.stereotype.Repository;

// A anotação @Repository diz ao Spring que esta é a implementação de um repositório.

@Repository
public class PassageiroRepositoryImpl {
    private final JpaPassageiroRepository jpaPassageiroRepository;
    private final PassageiroMapper passageiroMapper;

    public PassageiroRepositoryImpl(JpaPassageiroRepository jpaPassageiroRepository, PassageiroMapper passageiroMapper) {
        this.jpaPassageiroRepository = jpaPassageiroRepository;
        this.passageiroMapper = passageiroMapper;
    }

}
