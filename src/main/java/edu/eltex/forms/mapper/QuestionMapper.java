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
            @Mapping(ignore = true, target = "id"),
            @Mapping(ignore = true, target = "form"),
            @Mapping(source = "options", target = "options"),
    })
    Question toEntity(QuestionRequestDTO questionRequestDTO);

    @Mappings({
            @Mapping(source = "form.id", target = "formId"),
            @Mapping(source = "options", target = "options"),
    })
    QuestionResponseDTO toDto(Question questionEntity);
}
