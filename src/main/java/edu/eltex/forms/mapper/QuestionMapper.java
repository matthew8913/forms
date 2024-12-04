package edu.eltex.forms.mapper;

import edu.eltex.forms.dto.QuestionResponseDTO;
import edu.eltex.forms.entities.Question;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = OptionMapper.class)
public interface QuestionMapper {

    QuestionResponseDTO toDto(Question questionEntity);
}
