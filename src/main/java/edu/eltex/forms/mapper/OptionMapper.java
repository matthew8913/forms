package edu.eltex.forms.mapper;

import edu.eltex.forms.dto.OptionRequestDTO;
import edu.eltex.forms.dto.OptionResponseDTO;
import edu.eltex.forms.entities.Option;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OptionMapper {

    @Mapping(source = "question.id", target = "questionId")
    OptionResponseDTO toDto(Option optionEntity);

    @Mapping(source = "questionId", target = "question.id")
    Option toEntity(OptionRequestDTO optionRequestDTO);
}
