package br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.mapper;

import br.com.rodoviaria.spring_clean_arch.domain.entities.Onibus;
import br.com.rodoviaria.spring_clean_arch.domain.valueobjects.Placa;
import br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.model.OnibusModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OnibusPersistenceMapper {
    @Mapping(target = "id", source = "id") // <-- ADICIONE ESTA LINHA
    @Mapping(source = "placa.valor", target = "placa")
    @Mapping(target = "viagens", ignore = true)
    OnibusModel toModel(Onibus onibus);
    Onibus toDomain(OnibusModel onibusModel);

    default Placa mapStringToPlaca(String placa) {
        if (placa == null) {
            return null;
        }
        return new Placa(placa);
    }
}
