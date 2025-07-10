package br.com.rodoviaria.spring_clean_arch.application.mapper;

import br.com.rodoviaria.spring_clean_arch.application.dto.response.admin.AutenticarAdminResponse;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Administrador;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AdministradorMapper {
    AdministradorMapper INSTANCE = Mappers.getMapper(AdministradorMapper.class);

    @Mapping(source = "admin.email.email", target = "email") // Mapeia o VO para a String do DTO
    AutenticarAdminResponse toResponse(Administrador admin, String token);
}
