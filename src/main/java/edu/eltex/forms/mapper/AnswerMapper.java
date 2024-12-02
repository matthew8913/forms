package edu.eltex.forms.mapper;

import edu.eltex.forms.dto.AnswerResponseDTO;
import edu.eltex.forms.entities.Answer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = OptionMapper.class)
public interface AnswerMapper {

    AnswerResponseDTO toDto(Answer answerEntity);
}
