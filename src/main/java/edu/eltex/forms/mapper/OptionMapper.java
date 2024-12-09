package edu.eltex.forms.mapper;

import edu.eltex.forms.dto.OptionRequestDTO;
import edu.eltex.forms.dto.OptionResponseDTO;
import edu.eltex.forms.entities.Option;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OptionMapper {

    Option toEntity(OptionRequestDTO optionRequestDTO);

    @Mapping(source = "question.id", target = "questionId")
    OptionResponseDTO toDto(Option optionEntity);
}
