package edu.eltex.forms.mapper;

import edu.eltex.forms.dto.CompletionRequestDTO;
import edu.eltex.forms.dto.CompletionResponseDTO;
import edu.eltex.forms.entities.Completion;
import edu.eltex.forms.model.CompletionModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(
        componentModel = "spring",
        uses = {AnswerMapper.class, FormMapper.class, QuestionMapper.class, OptionMapper.class}
)
public interface CompletionMapper {

    @Mappings({
            @Mapping(source = "userId", target = "user.id"),
            @Mapping(source = "formId", target = "form.id"),
    })
    CompletionModel toModel(CompletionRequestDTO completionRequestDTO);

    @Mappings({
            @Mapping(source = "user.id", target = "userId"),
            @Mapping(source = "form.id", target = "formId"),
            @Mapping(source = "answers", target = "answers"),
    })
    CompletionResponseDTO toDTO(Completion completionEntity);

    @Mappings({
            @Mapping(source = "userId", target = "user.id"),
            @Mapping(source = "formId", target = "form.id"),
            @Mapping(source = "answers", target = "answers"),
    })
    Completion toEntity(CompletionRequestDTO formModel);

    Completion toEntity(CompletionModel formModel);


}
