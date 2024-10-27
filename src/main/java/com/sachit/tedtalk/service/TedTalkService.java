package com.sachit.tedtalk.service;

import com.sachit.tedtalk.entity.TedTalk;
import com.sachit.tedtalk.model.TedTalkMapper;
import com.sachit.tedtalk.model.TedTalkRequestDTO;
import com.sachit.tedtalk.model.TedTalkResponseDTO;
import com.sachit.tedtalk.repository.TedTalkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
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

    public TedTalkResponseDTO getTedTalk(Long id) throws Exception{
        Optional<TedTalk> existingTedTalk = tedTalkRepository.findById(id);
        if(existingTedTalk.isPresent())
            return tedTalkMapper.tedTalkToTedTalkResponseDTO(existingTedTalk.get());
        else
            throw new Exception("Ted Talk Not found with the given id");

    }

    public TedTalkResponseDTO createTedTalk(TedTalkRequestDTO tedTalkRequestDTO){
        return tedTalkMapper.tedTalkToTedTalkResponseDTO(tedTalkRepository.save(tedTalkMapper.tedTalkRequestToTedTalk(tedTalkRequestDTO)));
    }

    public TedTalkResponseDTO updateTedTalk(Long id, TedTalkRequestDTO tedTalkRequestDTO) throws Exception{
        Optional<TedTalk> existingTedTalk = tedTalkRepository.findById(id);
        if(existingTedTalk.isPresent()){
            TedTalk newTedTalk = tedTalkMapper.tedTalkRequestToTedTalk(tedTalkRequestDTO);
            newTedTalk.setId(existingTedTalk.get().getId());
            return tedTalkMapper.tedTalkToTedTalkResponseDTO(tedTalkRepository.save(newTedTalk));
        }
        else
            throw new Exception("Ted Talk Not found with the given id");

    }

    public void deleteTedTalk(Long id) throws Exception{
        try {
            tedTalkRepository.deleteById(id);
        }
        catch(Exception e){
            throw new Exception("Ted Talk Not found with the given id");
        }
    }

}
