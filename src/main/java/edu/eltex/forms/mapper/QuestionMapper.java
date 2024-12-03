package edu.eltex.forms.mapper;

import edu.eltex.forms.dto.QuestionRequestDTO;
import edu.eltex.forms.dto.QuestionResponseDTO;
import edu.eltex.forms.entities.Form;
import edu.eltex.forms.entities.Question;
import edu.eltex.forms.model.QuestionModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {OptionMapper.class, FormMapper.class})
public interface QuestionMapper {

    QuestionMapper INSTANCE = Mappers.getMapper(QuestionMapper.class);

    QuestionModel toModel(QuestionRequestDTO dto);

    QuestionResponseDTO toDto(QuestionModel questionModel);

    QuestionResponseDTO toDto(Question questionEntity);

    @Mappings({
            @Mapping(target = "formId", source = "form.id"),
            @Mapping(target = "type", source = "type", qualifiedByName = "typeToString")
    })
    QuestionModel toModel(Question question);

    @Mappings({
            @Mapping(target = "form", source = "formId", qualifiedByName = "formFromId"),
            @Mapping(target = "type", source = "type", qualifiedByName = "stringToType")
    })
    Question toEntity(QuestionModel questionModel);

    @Named("typeToString")
    default String typeToString(Question.Type type) {
        return type != null ? type.name() : null;
    }

    @Named("stringToType")
    default Question.Type stringToType(String type) {
        return type != null ? Question.Type.valueOf(type) : null;
    }

    @Named("formFromId")
    default Form formFromId(Integer formId) {
        return formId != null ? Form.builder().id(formId).build() : null;
    }
}
