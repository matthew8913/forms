package edu.eltex.forms.mapper;

import edu.eltex.forms.dto.OptionRequestDTO;
import edu.eltex.forms.dto.OptionResponseDTO;
import edu.eltex.forms.entities.Option;
import edu.eltex.forms.entities.Question;
import edu.eltex.forms.model.OptionModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OptionMapper {

    OptionMapper INSTANCE = Mappers.getMapper(OptionMapper.class);

    @Mappings({
            @Mapping(target = "question", source = "questionId", qualifiedByName = "questionFromId")
    })
    Option toEntity(OptionModel optionModel);

    OptionModel toModel(Option option);

    OptionModel toModel(OptionRequestDTO dto);

    OptionResponseDTO toDto(OptionModel optionModel);

    @Mapping(source = "text", target = "optionText")
    OptionResponseDTO toDto(Option optionEntity);

    @Named("questionFromId")
    default Question questionFromId(Integer questionId) {
        return questionId != null ? Question.builder().id(questionId).build() : null;
    }
}
