package edu.eltex.forms.mapper;

import edu.eltex.forms.dto.OptionResponseDTO;
import edu.eltex.forms.entities.Option;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OptionMapper {

    @Mapping(source = "text", target = "optionText")
    OptionResponseDTO toDto(Option optionEntity);
}
