package com.sachit.tedtalk.service;

import com.sachit.tedtalk.entity.TedTalk;
import com.sachit.tedtalk.exception.TedTalkExistsException;
import com.sachit.tedtalk.exception.TedTalkNotFoundException;
import com.sachit.tedtalk.model.TedTalkMapper;
import com.sachit.tedtalk.model.TedTalkRequestDTO;
import com.sachit.tedtalk.model.TedTalkResponseDTO;
import com.sachit.tedtalk.repository.TedTalkRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Slf4j
public class TedTalkService {

    @Autowired
    TedTalkRepository tedTalkRepository;

    @Autowired
    TedTalkMapper tedTalkMapper;


    public List<TedTalkResponseDTO> getAllTedTalks(){
                return StreamSupport.stream(tedTalkRepository.findAll().spliterator(),true)
                        .map(tedTalkMapper::tedTalkToTedTalkResponseDTO)
                        .collect(Collectors.toList());
    }

    public TedTalkResponseDTO getTedTalk(Long id) throws TedTalkNotFoundException{
        Optional<TedTalk> existingTedTalk = tedTalkRepository.findById(id);
        if(existingTedTalk.isPresent())
            return tedTalkMapper.tedTalkToTedTalkResponseDTO(existingTedTalk.get());
        else
            throw new TedTalkNotFoundException("Ted Talk Not found with the given id");

    }

    public TedTalkResponseDTO createTedTalk(TedTalkRequestDTO tedTalkRequestDTO) throws TedTalkExistsException {
        try {
            Optional<TedTalk> existingTedTalk = tedTalkRepository.findByTitle(tedTalkRequestDTO.getTitle());
            if(existingTedTalk.isPresent())
                throw new TedTalkExistsException("Ted Talk with the same title already exists");
            return tedTalkMapper.tedTalkToTedTalkResponseDTO(tedTalkRepository.save(tedTalkMapper.tedTalkRequestToTedTalk(tedTalkRequestDTO)));
        }
        catch (TedTalkExistsException e) {
            log.error(e.getMessage(),e);
            throw e;
        }
        catch (Exception e) {
            log.error("Unable to create ted talk");
            throw new RuntimeException("Unable to create ted talk");
        }
    }

    public TedTalkResponseDTO updateTedTalk(Long id, TedTalkRequestDTO tedTalkRequestDTO) throws TedTalkNotFoundException{
        try {
            if (tedTalkRepository.existsById(id)) {
                TedTalk newTedTalk = tedTalkMapper.tedTalkRequestToTedTalk(tedTalkRequestDTO);
                newTedTalk.setId(id);
                return tedTalkMapper.tedTalkToTedTalkResponseDTO(tedTalkRepository.save(newTedTalk));
            } else
                throw new TedTalkNotFoundException("Ted Talk Not found with the given id");
        } catch (TedTalkNotFoundException e) {
            log.error(e.getMessage(),e);
            throw e;
        }catch (Exception e) {
            log.error("Unable to update ted talk");
            throw new RuntimeException("Unable to update ted talk");
        }
    }

    public void deleteTedTalk(Long id) throws TedTalkNotFoundException{
            if(tedTalkRepository.existsById(id))
            tedTalkRepository.deleteById(id);
            else
                throw new TedTalkNotFoundException("Ted Talk Not Found with the given id");
    }

}
