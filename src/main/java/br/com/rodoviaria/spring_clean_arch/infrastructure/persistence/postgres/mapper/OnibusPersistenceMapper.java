package br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.mapper;

import br.com.rodoviaria.spring_clean_arch.domain.entities.Onibus;
import br.com.rodoviaria.spring_clean_arch.domain.valueobjects.Placa;
import br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.model.OnibusModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OnibusPersistenceMapper {

    // CORREÇÃO: Adicione esta anotação @Mapping
    /**
     * Diz ao MapStruct: "Para preencher o campo 'placa' do OnibusModel,
     * pegue o objeto 'placa' da entidade Onibus e acesse seu atributo 'valor'".
     */
    @Mapping(source = "placa.valor", target = "placa")
    OnibusModel toModel(Onibus onibus);

    // Para a conversão de volta, o seu método helper "mapStringToPlaca" já funciona bem.
    Onibus toDomain(OnibusModel onibusModel);

    // --- MÉTODOS DE CONVERSÃO ADICIONADOS ---
    // (O restante do seu arquivo está correto)

    default Placa mapStringToPlaca(String placa) {
        if (placa == null) {
            return null;
        }
        return new Placa(placa);
    }
}
