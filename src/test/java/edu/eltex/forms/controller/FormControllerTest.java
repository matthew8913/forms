package edu.eltex.forms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.eltex.forms.dto.FormResponseDTO;
import edu.eltex.forms.service.FormService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
        mockForms = List.of(
                FormResponseDTO.builder().id(1).title("Test title 1").creatorName("creator1").build(),
                FormResponseDTO.builder().id(2).title("Test title 2").build()
        );
    }

    @Test
    void getAllForms() throws Exception {
        Mockito.when(formService.getAllForms()).thenReturn(mockForms);

        String expectedJson = objectMapper.writeValueAsString(mockForms);

        mvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/forms")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(expectedJson));
    }

    @Test
    void getFormById() throws Exception {
        int id = 1;

        Mockito.when(formService.getFormById(id)).thenReturn(mockForms.getFirst());

        String expectedJson = objectMapper.writeValueAsString(mockForms.getFirst());

        mvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/forms/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(expectedJson));
    }

    @Test
    void getFormByTitle() throws Exception {
        String title = "Test title 1";

        Mockito.when(formService.getFormByTitle(title)).thenReturn(mockForms.getFirst());

        String expectedJson = objectMapper.writeValueAsString(mockForms.getFirst());

        mvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/forms?title=" + title)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(expectedJson));
    }

    @Test
    void getFormByCreatorName() throws Exception {
        String creatorName = "creator1";

        Mockito.when(formService.getFormByTitle(creatorName)).thenReturn(mockForms.getFirst());

        String expectedJson = objectMapper.writeValueAsString(mockForms.getFirst());

        mvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/forms?title=" + creatorName)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(expectedJson));
    }
}
