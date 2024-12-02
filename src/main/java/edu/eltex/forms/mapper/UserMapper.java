package edu.eltex.forms.mapper;

import edu.eltex.forms.dto.UserRequestDto;
import edu.eltex.forms.dto.UserResponseDto;
import edu.eltex.forms.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "role", target = "role", qualifiedByName = "roleToString")
    UserResponseDto toDto(User user);

    @Mapping(source = "role", target = "role", qualifiedByName = "stringToRole")
    User toEntity(UserResponseDto dto);

    @Mapping(source = "role", target = "role", qualifiedByName = "stringToRole")
    User toEntity(UserRequestDto dto);

    @Named("roleToString")
    default String roleToString(User.Role role) {
        return role != null ? role.name() : null;
    }

    @Named("stringToRole")
    default User.Role stringToRole(String role) {
        return role != null ? User.Role.valueOf(role) : null;
    }
}