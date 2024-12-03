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
            @Mapping(source = "", target = ""),
            @Mapping(source = "", target = ""),
    })
    CompletionModel toModel(CompletionRequestDTO completionRequestDTO);

    @Mappings({
            @Mapping(source = "", target = ""),
            @Mapping(source = "", target = ""),
    })
    CompletionResponseDTO toDTO(Completion completionEntity);

    Completion toEntity(CompletionModel formModel);
}
