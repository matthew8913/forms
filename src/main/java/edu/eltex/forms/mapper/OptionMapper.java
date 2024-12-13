package edu.eltex.forms.mapper;

import edu.eltex.forms.dto.OptionRequestDTO;
import edu.eltex.forms.dto.OptionResponseDTO;
import edu.eltex.forms.entities.Option;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface OptionMapper {

    @Mappings({
            @Mapping(ignore = true, target = "id"),
            @Mapping(ignore = true, target = "question")
    })
    Option toEntity(OptionRequestDTO optionRequestDTO);

    @Mapping(source = "question.id", target = "questionId")
    OptionResponseDTO toDto(Option optionEntity);
}
