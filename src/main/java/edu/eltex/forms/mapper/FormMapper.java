package edu.eltex.forms.mapper;

import edu.eltex.forms.dto.FormInfoResponseDTO;
import edu.eltex.forms.dto.FormRequestDTO;
import edu.eltex.forms.dto.FormResponseDTO;
import edu.eltex.forms.entities.Form;
import edu.eltex.forms.model.FormModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface FormMapper {

    FormMapper INSTANCE = Mappers.getMapper(FormMapper.class);

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

    @Mappings({
            @Mapping(source = "creator.id", target = "creatorId"),
            @Mapping(source = "creator.username", target = "creatorName")
    })
    FormResponseDTO toDto(FormModel formModel);

    @Mappings({
            @Mapping(source = "creator.id", target = "creatorId"),
            @Mapping(source = "creator.username", target = "creatorName")
    })
    FormInfoResponseDTO toInfoDto(Form form);

    Form toEntity(FormModel formModel);

    FormModel toModel(Form form);
}
