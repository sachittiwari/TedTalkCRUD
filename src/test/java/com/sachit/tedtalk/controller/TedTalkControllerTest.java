package com.sachit.tedtalk.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sachit.tedtalk.constants.TedTalkTestConstants;
import com.sachit.tedtalk.exception.TedTalkExistsException;
import com.sachit.tedtalk.exception.TedTalkNotFoundException;
import com.sachit.tedtalk.model.TedTalkResponseDTO;
import com.sachit.tedtalk.service.TedTalkService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.test.web.servlet.MockMvc;


import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TedTalkController.class)
public class TedTalkControllerTest {
    @MockBean
    TedTalkService tedTalkService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void testGetAllTedTalks_success() throws Exception {
        //Arrange data
        Pageable pageable = PageRequest.of(0, 10);
        Page<TedTalkResponseDTO> response = new PageImpl<>(List.of(TedTalkTestConstants.TED_TALK_1,TedTalkTestConstants.TED_TALK_2));

        //mock data
        when(tedTalkService.getAllTedTalks(pageable)).thenReturn(response);

        //Act and Assert
        mockMvc.perform(get("/tedtalk/")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].title").value("TedTalk 1"))
                .andExpect(jsonPath("$.content[0].author").value("Sachit"))
                .andExpect(jsonPath("$.content[0].date").value("01-2024"))
                .andExpect(jsonPath("$.content[0].views").value(100L))
                .andExpect(jsonPath("$.content[0].likes").value(500L))
                .andExpect(jsonPath("$.content[0].link").value("www.tedtalk1.com"))
                .andExpect(jsonPath("$.content[1].id").value(2L))
                .andExpect(jsonPath("$.content[1].title").value("TedTalk 2"))
                .andExpect(jsonPath("$.content[1].author").value("Tiwari"))
                .andExpect(jsonPath("$.content[1].date").value("02-2024"))
                .andExpect(jsonPath("$.content[1].views").value(200L))
                .andExpect(jsonPath("$.content[1].likes").value(700L))
                .andExpect(jsonPath("$.content[1].link").value("www.tedtalk2.com"));
    }

    @Test
    public void testGetAllTedTalks_throwsException() throws Exception {
        //Arrange data
        List<TedTalkResponseDTO> list = List.of(TedTalkTestConstants.TED_TALK_1,TedTalkTestConstants.TED_TALK_2);
        Pageable pageable = PageRequest.of(0, 10);
        Page<TedTalkResponseDTO> response = new PageImpl<>(list);

        //mock data
        when(tedTalkService.getAllTedTalks(pageable)).thenThrow(new RuntimeException("Unable to fetch the details"));

        //Act and Assert
        mockMvc.perform(get("/tedtalk/")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().is5xxServerError());
    }

    @Test
    public void testGetTedTalkById_success() throws Exception {
        //mock data
        when(tedTalkService.getTedTalk(1L)).thenReturn(TedTalkTestConstants.TED_TALK_1);

        //Act and Assert
        mockMvc.perform(get("/tedtalk/{id}",1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("TedTalk 1"))
                .andExpect(jsonPath("$.author").value("Sachit"))
                .andExpect(jsonPath("$.date").value("01-2024"))
                .andExpect(jsonPath("$.views").value(100L))
                .andExpect(jsonPath("$.likes").value(500L))
                .andExpect(jsonPath("$.link").value("www.tedtalk1.com"));

    }

    @Test
    public void testGetTedTalkById_NotFound() throws Exception {
        //mock data
        when(tedTalkService.getTedTalk(3L)).thenThrow(new TedTalkNotFoundException(TedTalkTestConstants.TED_TALK_NOT_FOUND));

        //Act and Assert
        mockMvc.perform(get("/tedtalk/{id}",3L))
                .andExpect(status().isNotFound());

    }

    @Test
    public void createTedTalk_success() throws Exception {
        //Arrange
        TedTalkResponseDTO response = new TedTalkResponseDTO(1L,"New ted Talk","New Author","09-2024",500L,600L,"www.newtedtalk.com");

        //mock data
        when(tedTalkService.createTedTalk(TedTalkTestConstants.NEW_TED_TALK)).thenReturn(response);

        //Act and Assert
        mockMvc.perform(post("/tedtalk/")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(TedTalkTestConstants.NEW_TED_TALK)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.getId()))
                .andExpect(jsonPath("$.title").value(response.getTitle()))
                .andExpect(jsonPath("$.author").value(response.getAuthor()))
                .andExpect(jsonPath("$.date").value(response.getDate()))
                .andExpect(jsonPath("$.views").value(response.getViews()))
                .andExpect(jsonPath("$.likes").value(response.getLikes()))
                .andExpect(jsonPath("$.link").value(response.getLink()));

    }

    @Test
    public void createTedTalk_recordExists() throws Exception {
        //mock data
        when(tedTalkService.createTedTalk(TedTalkTestConstants.NEW_TED_TALK)).thenThrow(new TedTalkExistsException(TedTalkTestConstants.TED_TALK_EXISTS));

        //Act and Assert
        mockMvc.perform(post("/tedtalk/")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(TedTalkTestConstants.NEW_TED_TALK)))
                .andExpect(status().is5xxServerError());

    }

    @Test
    public void createTedTalk_throws500Error() throws Exception {
        //mock data
        when(tedTalkService.createTedTalk(TedTalkTestConstants.NEW_TED_TALK)).thenThrow(new RuntimeException("Unable to create Ted Talk"));

        //Act and Assert
        mockMvc.perform(post("/tedtalk/")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(TedTalkTestConstants.NEW_TED_TALK)))
                .andExpect(status().is5xxServerError());

    }

    @Test
    public void updateTedTalk_success() throws Exception {
        //Arrange data
        TedTalkResponseDTO response = new TedTalkResponseDTO(1L,"New ted Talk","New Author","09-2024",500L,600L,"www.newtedtalk.com");

        //mock data
        when(tedTalkService.updateTedTalk(1L,TedTalkTestConstants.NEW_TED_TALK)).thenReturn(response);

        //Act and Assert
        mockMvc.perform(put("/tedtalk/{id}",1L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(TedTalkTestConstants.NEW_TED_TALK)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.getId()))
                .andExpect(jsonPath("$.title").value(response.getTitle()))
                .andExpect(jsonPath("$.author").value(response.getAuthor()))
                .andExpect(jsonPath("$.date").value(response.getDate()))
                .andExpect(jsonPath("$.views").value(response.getViews()))
                .andExpect(jsonPath("$.likes").value(response.getLikes()))
                .andExpect(jsonPath("$.link").value(response.getLink()));

    }

    @Test
    public void updateTedTalk_notFound() throws Exception {
        //mock data
        when(tedTalkService.updateTedTalk(1L,TedTalkTestConstants.NEW_TED_TALK)).thenThrow(new TedTalkNotFoundException(TedTalkTestConstants.TED_TALK_NOT_FOUND));

        //Act and Assert
        mockMvc.perform(put("/tedtalk/{id}",1L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(TedTalkTestConstants.NEW_TED_TALK)))
                .andExpect(status().isNotFound());

    }

    @Test
    public void updateTedTalk_throwsError() throws Exception {
        //mock data
        when(tedTalkService.updateTedTalk(1L,TedTalkTestConstants.NEW_TED_TALK)).thenThrow(new RuntimeException("Unable to update ted talk"));

        //Act and Assert
        mockMvc.perform(put("/tedtalk/{id}",1L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(TedTalkTestConstants.NEW_TED_TALK)))
                .andExpect(status().is5xxServerError());

    }

    @Test
    public void deleteTedTalk_success() throws Exception {
        //mock data
        doNothing().when(tedTalkService).deleteTedTalk(1L);

        //Act and Assert
        mockMvc.perform(delete("/tedtalk/{id}",1L))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteTedTalk_notFound() throws Exception {
        //mock data
        doThrow(new TedTalkNotFoundException(TedTalkTestConstants.TED_TALK_NOT_FOUND)).when(tedTalkService).deleteTedTalk(1L);

        //Act and Assert
        mockMvc.perform(delete("/tedtalk/{id}",1L))
                .andExpect(status().isNotFound());
    }

    @Test
    public void searchTedTalk_success() throws Exception {
        //Arrange data
        Pageable pageable = PageRequest.of(0, 10);
        Page<TedTalkResponseDTO> response = new PageImpl<>(List.of(TedTalkTestConstants.TED_TALK_1));

        //mock data
        when(tedTalkService.searchTedTalks("Sachit","TedTalk",100L,500L,pageable)).thenReturn(response);

        //Act and Assert
        mockMvc.perform(get("/tedtalk/search")
                        .param("page", "0")
                        .param("size", "10")
                        .param("author", "Sachit")
                        .param("title", "TedTalk")
                        .param("minLikes", "500")
                        .param("minViews", "100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].title").value("TedTalk 1"))
                .andExpect(jsonPath("$.content[0].author").value("Sachit"))
                .andExpect(jsonPath("$.content[0].date").value("01-2024"))
                .andExpect(jsonPath("$.content[0].views").value(100L))
                .andExpect(jsonPath("$.content[0].likes").value(500L))
                .andExpect(jsonPath("$.content[0].link").value("www.tedtalk1.com"));

    }

    @Test
    public void searchTedTalk_notFound() throws Exception {
        //Arrange data
        Pageable pageable = PageRequest.of(0, 10);
        Page<TedTalkResponseDTO> response = new PageImpl<>(List.of());

        //mock data
        when(tedTalkService.searchTedTalks("Sachit","TedTalk",100L,500L,pageable)).thenReturn(response);

        //Act and Assert
        mockMvc.perform(get("/tedtalk/search")
                        .param("page", "0")
                        .param("size", "10")
                        .param("author", "Sachit")
                        .param("title", "TedTalk")
                        .param("minLikes", "500")
                        .param("minViews", "100"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void searchTedTalk_throwsError() throws Exception {
        //Arrange data
        Pageable pageable = PageRequest.of(0, 10);

        //mock data
        when(tedTalkService.searchTedTalks("Sachit","TedTalk",100L,500L,pageable)).thenThrow(new RuntimeException("Unable to fetch ted talk details"));

        //Act and Assert
        mockMvc.perform(get("/tedtalk/search")
                        .param("page", "0")
                        .param("size", "10")
                        .param("author", "Sachit")
                        .param("title", "TedTalk")
                        .param("minLikes", "500")
                        .param("minViews", "100"))
                .andExpect(status().is5xxServerError());
    }


}
