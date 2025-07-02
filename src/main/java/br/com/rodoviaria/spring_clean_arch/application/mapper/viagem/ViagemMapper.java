package br.com.rodoviaria.spring_clean_arch.application.mapper.viagem;

import br.com.rodoviaria.spring_clean_arch.application.dto.response.viagem.ViagemPorLinhaResponse;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.viagem.ViagemPorPassageiroResponse;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.viagem.ViagemResponse;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.viagem.ViagensDisponiveisResponse;
import br.com.rodoviaria.spring_clean_arch.application.mapper.linha.LinhaMapper;
import br.com.rodoviaria.spring_clean_arch.application.mapper.onibus.OnibusMapper;
import br.com.rodoviaria.spring_clean_arch.application.mapper.passageiro.PassageiroMapper;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Viagem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
// 1. ANOTAÇÃO PRINCIPAL:
// Diz ao MapStruct para processar esta interface.
// O 'uses' diz que este mapper pode precisar usar outros mappers para completar seu trabalho.
@Mapper(uses = { OnibusMapper.class, LinhaMapper.class, PassageiroMapper.class })
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

    // --- NOVO MÉTODO PARA O NOSSO CASO DE USO ---
    // Ele vai converter a entidade Viagem para o DTO ViagemPorLinhaResponse
    @Mapping(source = "onibus.id", target = "onibusId") // Pega só o ID do ônibus
    @Mapping(source = "dataHoraChegada", target = "dataChegada") // Corrige o nome do campo
    ViagemPorLinhaResponse toViagemPorLinhaResponse(Viagem viagem);


    // --- NOVO MÉTODO PARA O NOSSO CASO DE USO ---
    // O MapStruct vai usar os mappers de Onibus, Linha e Passageiro
    // automaticamente, pois nós os declaramos na anotação @Mapper(uses=...).
    // Não precisamos de nenhuma anotação @Mapping aqui!
    ViagemPorPassageiroResponse toViagemPorPassageiroResponse(Viagem viagem);

    // --- NOVO MÉTODO PARA O BUSCARVIAGENSDISPONIVEISUSECASE ---
    @Mapping(source = "dataPartida", target = "data")           // Mapeia dataPartida da entidade para data no DTO
    @Mapping(source = "linha.origem", target = "origem")       // Pega o campo aninhado 'origem' de dentro da 'linha'
    @Mapping(source = "linha.destino", target = "destino")     // Pega o campo aninhado 'destino' de dentro da 'linha'
    @Mapping(source = "onibus", target = "onibus")             // Usa o OnibusMapper para converter Onibus -> OnibusResponse
    ViagensDisponiveisResponse toViagensDisponiveisResponse(Viagem viagem);
}
