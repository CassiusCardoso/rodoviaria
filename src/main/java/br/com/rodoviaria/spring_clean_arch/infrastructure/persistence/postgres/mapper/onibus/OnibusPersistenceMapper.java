package br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.mapper.onibus;

import br.com.rodoviaria.spring_clean_arch.domain.entities.Onibus;
import br.com.rodoviaria.spring_clean_arch.domain.valueobjects.onibus.Placa;
import br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.model.OnibusModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OnibusPersistenceMapper {

    // CORREÇÃO: Usar Mappers.getMapper() para obter a instância
    OnibusPersistenceMapper INSTANCE = Mappers.getMapper(OnibusPersistenceMapper.class);
    OnibusModel toModel(Onibus onibus);
    Onibus toDomain(OnibusModel onibusModel);

    // --- MÉTODOS DE CONVERSÃO ADICIONADOS ---

    /**
     * Converte o objeto de valor Placa em uma String.
     * O MapStruct usará este método ao chamar toModel().
     */
    default String mapPlacaToString(Placa placa) {
        if (placa == null) {
            return null;
        }
        // Usa o método getValor() da sua classe Placa
        return placa.getValor();
    }

    /**
     * Converte uma String no objeto de valor Placa.
     * O MapStruct usará este método ao chamar toDomain().
     */
    default Placa mapStringToPlaca(String placa) {
        if (placa == null) {
            return null;
        }
        // Usa o construtor da sua classe Placa
        return new Placa(placa);
    }
}
