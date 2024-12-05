package edu.eltex.forms.mapper;

import edu.eltex.forms.dto.QuestionRequestDTO;
import edu.eltex.forms.dto.QuestionResponseDTO;
import edu.eltex.forms.entities.Question;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = OptionMapper.class)
public interface QuestionMapper {

    @Mappings({
            @Mapping(source = "form.id", target = "formId"),
            @Mapping(source = "options", target = "options"),
    })
    QuestionResponseDTO toDto(Question questionEntity);

    @Mappings({
            @Mapping(source = "formId", target = "form.id"),
            @Mapping(source = "options", target = "options"),
    })
    Question toEntity(QuestionRequestDTO questionRequestDTO);
}
