package edu.eltex.forms.mapper;

import edu.eltex.forms.dto.FormRequestDTO;
import edu.eltex.forms.dto.FormResponseDTO;
import edu.eltex.forms.entities.Form;
import edu.eltex.forms.model.FormModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {OptionMapper.class, QuestionMapper.class})
public interface FormMapper {

    @Mappings({
            @Mapping(source = "creatorId", target = "creator.id"),
            @Mapping(source = "creatorName", target = "creator.username"),
    })
    FormModel toModel(FormRequestDTO formRequestDTO);

    @Mappings({
            @Mapping(source = "creator.id", target = "creatorId"),
            @Mapping(source = "creator.username", target = "creatorName"),
            @Mapping(source = "questions", target = "questions")
    })
    FormResponseDTO toDto(Form formEntity);

    Form toEntity(FormModel formModel);
}
