package edu.eltex.forms.mapper;

import edu.eltex.forms.dto.UserRequestDto;
import edu.eltex.forms.dto.UserResponseDto;
import edu.eltex.forms.entities.User;
import edu.eltex.forms.enums.UserRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mappings({
            @Mapping(ignore = true, target = "password"),
            @Mapping(ignore = true, target = "refreshToken"),
            @Mapping(source = "role", target = "role", qualifiedByName = "stringToRole"),
    })
    User toEntity(UserResponseDto dto);

    @Mappings({
            @Mapping(ignore = true, target = "id"),
            @Mapping(ignore = true, target = "refreshToken"),
            @Mapping(source = "role", target = "role", qualifiedByName = "stringToRole"),
    })
    User toEntity(UserRequestDto dto);

    @Mapping(source = "role", target = "role", qualifiedByName = "roleToString")
    UserResponseDto toDto(User user);

    @Named("roleToString")
    default String roleToString(UserRole role) {
        return role != null ? role.name() : null;
    }

    @Named("stringToRole")
    default UserRole stringToRole(String role) {
        return role != null ? UserRole.valueOf(role) : null;
    }
}