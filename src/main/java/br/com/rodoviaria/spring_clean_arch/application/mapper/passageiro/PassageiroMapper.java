package br.com.rodoviaria.spring_clean_arch.application.mapper.passageiro;

import br.com.rodoviaria.spring_clean_arch.application.dto.response.passageiro.AtualizarInformacoesPassageiroResponse;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.passageiro.PassageiroPorViagemResponse;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.passageiro.PassageiroResponse;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Passageiro;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PassageiroMapper {
    PassageiroMapper INSTANCE = Mappers.getMapper(PassageiroMapper.class);

    // Mapeamento para o DTO principal
    @Mapping(source = "email.email", target = "email")
    @Mapping(source = "cpf.cpf", target = "cpf")
    @Mapping(source = "telefone.telefone", target = "telefone")
    PassageiroResponse toResponse(Passageiro passageiro);

    // Mapeamento para o DTO de resposta da atualização
    @Mapping(source = "email.email", target = "email")
    @Mapping(source = "cpf.cpf", target = "cpf")
    @Mapping(source = "telefone.telefone", target = "telefone")
    AtualizarInformacoesPassageiroResponse toAtualizarInformacoesResponse(Passageiro passageiro);

    // O MapStruct mapeia 'id', 'nome', 'role', 'ativo' e 'criadoEm' automaticamente
    // em ambos os métodos porque os nomes dos campos coincidem.
}