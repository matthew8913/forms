package edu.eltex.forms.mapper;

import edu.eltex.forms.dto.FormRequestDTO;
import edu.eltex.forms.dto.FormResponseDTO;
import edu.eltex.forms.entities.Form;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {QuestionMapper.class})
public interface FormMapper {

    @Mappings({
            @Mapping(source = "creator.id", target = "creatorId"),
            @Mapping(source = "creator.username", target = "creatorName"),
            @Mapping(source = "questions", target = "questions")
    })
    FormResponseDTO toDto(Form formEntity);

    @Mappings({
            @Mapping(source = "creatorId", target = "creator.id"),
            @Mapping(source = "creatorName", target = "creator.username"),
            @Mapping(source = "questions", target = "questions")
    })
    Form toEntity(FormRequestDTO formRequestDTO);
}
