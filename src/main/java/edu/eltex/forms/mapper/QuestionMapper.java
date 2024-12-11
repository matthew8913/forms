package edu.eltex.forms.mapper;

import edu.eltex.forms.dto.QuestionRequestDTO;
import edu.eltex.forms.dto.QuestionResponseDTO;
import edu.eltex.forms.entities.Question;
import org.mapstruct.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Mapper(componentModel = "spring", uses = OptionMapper.class, builder = @Builder(disableBuilder = true))
public interface QuestionMapper {

    @Mappings({
            @Mapping(ignore = true, target = "id"),
            @Mapping(ignore = true, target = "form"),
            @Mapping(ignore = true, target = "imageUrl"),
            @Mapping(source = "options", target = "options"),
    })
    Question toEntity(QuestionRequestDTO questionRequestDTO);

    @Mappings({
            @Mapping(source = "form.id", target = "formId"),
            @Mapping(source = "options", target = "options"),
    })
    QuestionResponseDTO toDto(Question questionEntity);

    @AfterMapping
    default void handleImage(@MappingTarget Question question, QuestionRequestDTO questionRequestDTO) {
        MultipartFile image = questionRequestDTO.getImage();
        if (image != null && !image.isEmpty()) {
            try {
                boolean isWin = System.getProperty("os.name").toLowerCase().contains("win");
                String basePath = (isWin) ? "C:\\uploads\\images" : "/uploads/images";

                String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
                Path filePath = Paths.get(basePath, fileName);
                Files.createDirectories(filePath.getParent());
                image.transferTo(filePath.toFile());
                question.setImageUrl("/images/" + fileName);
            } catch (IOException e) {
                throw new RuntimeException("Failed to save file", e);
            }
        }
    }
}
