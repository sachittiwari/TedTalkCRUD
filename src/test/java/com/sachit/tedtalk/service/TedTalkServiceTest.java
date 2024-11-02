package com.sachit.tedtalk.service;

import com.sachit.tedtalk.constants.TedTalkTestConstants;
import com.sachit.tedtalk.entity.TedTalk;
import com.sachit.tedtalk.exception.TedTalkExistsException;
import com.sachit.tedtalk.exception.TedTalkNotFoundException;
import com.sachit.tedtalk.model.TedTalkMapper;
import com.sachit.tedtalk.model.TedTalkResponseDTO;
import com.sachit.tedtalk.repository.TedTalkRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TedTalkServiceTest {

    @MockBean
    TedTalkRepository tedTalkRepository;

    @Autowired
    TedTalkMapper tedTalkMapper;

    @Autowired
    private TedTalkService tedTalkService;

    @Test
    public void testGetAllTedTalks_returnResponse() {
        //Arrange data
        Pageable pageable = PageRequest.of(0, 10);
        Page<TedTalk> response = new PageImpl<>(List.of(TedTalkTestConstants.TED_TALK_ENTITY_1, TedTalkTestConstants.TED_TALK_ENTITY_2));

        //mock data
        when(tedTalkRepository.findAll(pageable)).thenReturn(response);

        //Act
        Page<TedTalkResponseDTO> result = tedTalkService.getAllTedTalks(pageable);

        //Assert
        assertEquals(result.getTotalElements(),2);
        verify(tedTalkRepository).findAll(pageable);
    }

    @Test
    public void testGetTedTalkById_throwException(){
        //Arrange data
        Optional<TedTalk> tedTalk = Optional.empty();

        //mock data
        when(tedTalkRepository.findById(1L)).thenReturn(tedTalk);

        //Act and Assert
        assertThrows(TedTalkNotFoundException.class,()->{tedTalkService.getTedTalk(1L);});
        verify(tedTalkRepository).findById(1L);
    }

    @Test
    public void testGetTedTalkById_returnResponse() throws TedTalkNotFoundException {
        //Arrange data
        Optional<TedTalk> tedTalk = Optional.of(TedTalkTestConstants.TED_TALK_ENTITY_1);

        //mock data
        when(tedTalkRepository.findById(1L)).thenReturn(tedTalk);

        //Act
        TedTalkResponseDTO result = tedTalkService.getTedTalk(1L);

        //Assert
        assertEquals(result.getAuthor(),"Sachit");
        assertEquals(result.getTitle(),"TedTalk 1");
        assertEquals(result.getDate(),"01-2024");
        assertEquals(result.getViews(),100L);
        assertEquals(result.getLikes(),500L);
        assertEquals(result.getLink(),"www.tedtalk1.com");

        verify(tedTalkRepository).findById(1L);
    }

    @Test
    public void testCreateTedTalk_returnResponse() throws TedTalkExistsException {
        //Arrange data
        Optional<TedTalk> tedTalk = Optional.empty();
        TedTalk response = new TedTalk(1L,"New ted Talk","New Author","09-2024",500L,600L,"www.newtedtalk.com");

        //mock data
        when(tedTalkRepository.findByTitle("New ted Talk")).thenReturn(tedTalk);
        when(tedTalkRepository.save(tedTalkMapper.tedTalkRequestToTedTalk(TedTalkTestConstants.NEW_TED_TALK))).thenReturn(response);

        //Act
        TedTalkResponseDTO result = tedTalkService.createTedTalk(TedTalkTestConstants.NEW_TED_TALK);

        //Assert
        assertEquals(result.getAuthor(),"New Author");
        assertEquals(result.getTitle(),"New ted Talk");
        assertEquals(result.getDate(),"09-2024");
        assertEquals(result.getViews(),500L);
        assertEquals(result.getLikes(),600L);
        assertEquals(result.getLink(),"www.newtedtalk.com");
        verify(tedTalkRepository).findByTitle("New ted Talk");
        verify(tedTalkRepository).save(tedTalkMapper.tedTalkRequestToTedTalk(TedTalkTestConstants.NEW_TED_TALK));
    }

    @Test
    public void testCreateTedTalk_throwException(){
        //Arrange data
        Optional<TedTalk> tedTalk = Optional.of(TedTalkTestConstants.TED_TALK_ENTITY_1);

        //mock data
        when(tedTalkRepository.findByTitle("New ted Talk")).thenReturn(tedTalk);

        //Act and Assert
        assertThrows(TedTalkExistsException.class,()->{tedTalkService.createTedTalk(TedTalkTestConstants.NEW_TED_TALK);});
        verify(tedTalkRepository).findByTitle("New ted Talk");
    }

    @Test
    public void testUpdateTedTalk_returnResponse() throws TedTalkNotFoundException {
        //Arrange data
        Long id = 1L;
        TedTalk updatedTedTalk = tedTalkMapper.tedTalkRequestToTedTalk(TedTalkTestConstants.NEW_TED_TALK);
        updatedTedTalk.setId(id);
        TedTalk response = new TedTalk(1L,"New ted Talk","New Author","09-2024",500L,600L,"www.newtedtalk.com");

        //mock data
        when(tedTalkRepository.existsById(id)).thenReturn(true);
        when(tedTalkRepository.save(updatedTedTalk)).thenReturn(response);

        //Act
        TedTalkResponseDTO result = tedTalkService.updateTedTalk(id,TedTalkTestConstants.NEW_TED_TALK);

        //Assert
        assertEquals(result.getAuthor(),"New Author");
        assertEquals(result.getTitle(),"New ted Talk");
        assertEquals(result.getDate(),"09-2024");
        assertEquals(result.getViews(),500L);
        assertEquals(result.getLikes(),600L);
        assertEquals(result.getLink(),"www.newtedtalk.com");
        verify(tedTalkRepository).existsById(id);
        verify(tedTalkRepository).save(updatedTedTalk);
    }

    @Test
    public void testUpdateTedTalk_throwException(){
        //mock data
        when(tedTalkRepository.existsById(1L)).thenReturn(false);

        //Act and Assert
        assertThrows(TedTalkNotFoundException.class,()->{tedTalkService.updateTedTalk(1L,TedTalkTestConstants.NEW_TED_TALK);});
        verify(tedTalkRepository).existsById(1L);
    }

    @Test
    public void testDeleteTedTalk_success() throws TedTalkNotFoundException {
        //Arrange data
        Long id = 1L;

        //mock data
        when(tedTalkRepository.existsById(id)).thenReturn(true);
        doNothing().when(tedTalkRepository).deleteById(id);

        //Act
        tedTalkService.deleteTedTalk(id);

        //Assert
        verify(tedTalkRepository).existsById(id);
        verify(tedTalkRepository).deleteById(id);
    }

    @Test
    public void testDeleteTedTalk_NotFound(){
        //mock data
        when(tedTalkRepository.existsById(1L)).thenReturn(false);

        //Act and Assert
        assertThrows(TedTalkNotFoundException.class,()->{tedTalkService.deleteTedTalk(1L);});
        verify(tedTalkRepository).existsById(1L);
    }

    @Test
    public void testSearchTedTalk_returnResponse() {
        //Arrange data
        Pageable pageable = PageRequest.of(0, 10);
        Page<TedTalk> response = new PageImpl<>(List.of(TedTalkTestConstants.TED_TALK_ENTITY_1, TedTalkTestConstants.TED_TALK_ENTITY_2));

        //mock data
        when(tedTalkRepository.findAll(any(),eq(pageable))).thenReturn(response);

        //Act
        Page<TedTalkResponseDTO> result = tedTalkService.searchTedTalks("Sachit","TedTalk",50L,50L,pageable);

        //Assert
        assertEquals(result.getTotalElements(),2);
        verify(tedTalkRepository).findAll(any(),eq(pageable));
    }


}
