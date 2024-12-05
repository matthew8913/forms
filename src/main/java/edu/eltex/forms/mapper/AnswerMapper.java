package edu.eltex.forms.mapper;

import edu.eltex.forms.dto.AnswerRequestDTO;
import edu.eltex.forms.dto.AnswerResponseDTO;
import edu.eltex.forms.entities.Answer;
import edu.eltex.forms.model.AnswerModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {QuestionMapper.class, OptionMapper.class})
public interface AnswerMapper {

    @Mappings({
            @Mapping(source = "completion.id", target = "completionId"),
            @Mapping(source = "question.id", target = "questionId"),
            @Mapping(source = "selectedOptions", target = "selectedOptions"),
    })
    AnswerResponseDTO toDto(Answer answerEntity);

    @Mappings({
            @Mapping(source = "completionId", target = "completion.id"),
            @Mapping(source = "questionId", target = "question.id"),
            @Mapping(source = "selectedOptions", target = "selectedOptions"),
    })
    Answer toEntity(AnswerRequestDTO answerRequestDTO);

    @Mappings({
            @Mapping(source = "completionId", target = "completion.id"),
            @Mapping(source = "questionId", target = "question.id"),
            @Mapping(source = "selectedOptions", target = "selectedOptions"),
    })
    AnswerModel toModel(AnswerRequestDTO answerRequestDTO);
}
