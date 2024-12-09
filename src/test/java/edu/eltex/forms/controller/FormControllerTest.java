package edu.eltex.forms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.eltex.forms.dto.FormRequestDTO;
import edu.eltex.forms.dto.FormResponseDTO;
import edu.eltex.forms.dto.QuestionRequestDTO;
import edu.eltex.forms.dto.QuestionResponseDTO;
import edu.eltex.forms.entities.User;
import edu.eltex.forms.enums.QuestionType;
import edu.eltex.forms.enums.UserRole;
import edu.eltex.forms.service.FormService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class FormControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private FormService formService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private List<FormResponseDTO> mockForms;

    @BeforeEach
    void setUp() {
        User u1 = new User(1, "user1", "pass1", UserRole.CREATOR, "rt");
        mockForms = List.of(
                FormResponseDTO.builder()
                        .id(1)
                        .title("Test title 1")
                        .description("Test description 1")
                        .creatorId(u1.getId())
                        .creatorName(u1.getUsername())
                        .build(),
                FormResponseDTO.builder()
                        .id(2)
                        .title("Test title 2")
                        .description("Test description 2")
                        .creatorId(u1.getId())
                        .creatorName(u1.getUsername())
                        .build()
        );
    }

    @Test
    void getAllForms() throws Exception {
        when(formService.getAllForms()).thenReturn(mockForms);

        String expectedJson = objectMapper.writeValueAsString(mockForms);

        mvc.perform(get("/api/v1/forms").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson))
                .andExpect(jsonPath("$[*].creatorName").isNotEmpty())
                .andExpect(jsonPath("$[*].title").isNotEmpty())
                .andExpect(jsonPath("$[*].description").isNotEmpty());
    }

    @Test
    void getFormById() throws Exception {
        int id = 1;

        when(formService.getFormById(id)).thenReturn(mockForms.getFirst());

        String expectedJson = objectMapper.writeValueAsString(mockForms.getFirst());

        mvc.perform(get("/api/v1/forms/" + id).accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson))
                .andExpect(jsonPath("$.creatorName").isNotEmpty())
                .andExpect(jsonPath("$.title").isNotEmpty())
                .andExpect(jsonPath("$.description").isNotEmpty());
    }

    @Test
    void getFormByTitle() throws Exception {
        String title = "Test title 1";

        when(formService.getFormByTitle(title)).thenReturn(mockForms.getFirst());

        String expectedJson = objectMapper.writeValueAsString(mockForms.getFirst());

        mvc.perform(get("/api/v1/forms?title=" + title).accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson))
                .andExpect(jsonPath("$.creatorName").isNotEmpty())
                .andExpect(jsonPath("$.title").isNotEmpty())
                .andExpect(jsonPath("$.description").isNotEmpty())
                .andExpect(jsonPath("$.title").value(title));
    }

    @Test
    void getFormsByCreatorName() throws Exception {
        String creatorName = "user1";

        when(formService.getAllFormsByCreatorName(creatorName)).thenReturn(mockForms);

        String expectedJson = objectMapper.writeValueAsString(mockForms);

        mvc.perform(get("/api/v1/forms?username=" + creatorName).accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson))
                .andExpect(jsonPath("$[*].creatorName").isNotEmpty())
                .andExpect(jsonPath("$[*].title").isNotEmpty())
                .andExpect(jsonPath("$[*].description").isNotEmpty())
                .andExpect(jsonPath("$[0].creatorName").value(creatorName));
    }

    @Test
    void createForm() throws Exception {
        User u = new User(1, "user1", "pass1", UserRole.CREATOR, "rt");
        FormRequestDTO fReq = FormRequestDTO.builder()
                .creatorId(u.getId())
                .title("Test title 1")
                .description("Test description 1")
                .questions(List.of(QuestionRequestDTO.builder().text("Q1").type(QuestionType.TEXT).build()))
                .build();
        FormResponseDTO fRes = FormResponseDTO.builder()
                .creatorId(fReq.getCreatorId())
                .creatorName(u.getUsername())
                .title(fReq.getTitle())
                .description(fReq.getDescription())
                .questions(List.of(QuestionResponseDTO.builder().text("Q1").type(QuestionType.TEXT).build()))
                .build();

        when(formService.createForm(fReq)).thenReturn(fRes);

        String requestStr = objectMapper.writeValueAsString(fReq);
        String responseStr = objectMapper.writeValueAsString(fRes);

        mvc.perform(post("/api/v1/forms").contentType(MediaType.APPLICATION_JSON).content(requestStr))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(responseStr));
    }

    @Test
    void deleteForm() throws Exception {
        int id = 1;

        when(formService.deleteForm(id)).thenReturn(true);

        mvc.perform(delete("/api/v1/forms/" + id).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void deleteForm_shouldReturn405StatusCode() throws Exception {
        when(formService.deleteForm(anyInt())).thenReturn(true);

        mvc.perform(delete("/api/v1/forms/").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }
}
