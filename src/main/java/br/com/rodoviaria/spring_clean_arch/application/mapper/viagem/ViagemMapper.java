package br.com.rodoviaria.spring_clean_arch.application.mapper.viagem;

import br.com.rodoviaria.spring_clean_arch.application.dto.response.viagem.ViagemResponse;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Viagem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
// 1. ANOTAÇÃO PRINCIPAL:
// Diz ao MapStruct para processar esta interface.
// O 'uses' diz que este mapper pode precisar usar outros mappers para completar seu trabalho.
public interface ViagemMapper {

    // 2. INSTÂNCIA DE ACESSO:
    // Permite que a gente chame o mapper de forma fácil: ViagemMapper.INSTANCE.toResponse(...)
    ViagemMapper INSTANCE = Mappers.getMapper(ViagemMapper.class);

    // Agora sem as anotações @Mapping desnecessárias!
    // O MapStruct vai associar todos os campos automaticamente:
    // id -> id
    // dataPartida -> dataPartida
    // dataHoraChegada -> dataHoraChegada
    // statusViagem -> statusViagem (Enum para String)
    // onibus -> onibus (usando o OnibusMapper)
    // linha -> linha (usando o LinhaMapper)@Mapping(source="data_hora_chegada", target="dataHoraChegada")
    ViagemResponse toResponse(Viagem viagem);
}
