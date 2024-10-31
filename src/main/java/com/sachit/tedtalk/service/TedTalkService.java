package com.sachit.tedtalk.service;

import com.sachit.tedtalk.entity.TedTalk;
import com.sachit.tedtalk.exception.TedTalkExistsException;
import com.sachit.tedtalk.exception.TedTalkNotFoundException;
import com.sachit.tedtalk.model.TedTalkMapper;
import com.sachit.tedtalk.model.TedTalkRequestDTO;
import com.sachit.tedtalk.model.TedTalkResponseDTO;
import com.sachit.tedtalk.repository.TedTalkRepository;
import com.sachit.tedtalk.repository.TedTalkSpecification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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

    private static final String TED_TALK_NOT_FOUND = "Ted Talk Not found with the given id";


    public Page<TedTalkResponseDTO> getAllTedTalks(Pageable pageable){
                return tedTalkRepository.findAll(pageable)
                        .map(tedTalkMapper::tedTalkToTedTalkResponseDTO);
    }

    public TedTalkResponseDTO getTedTalk(Long id) throws TedTalkNotFoundException{
        Optional<TedTalk> existingTedTalk = tedTalkRepository.findById(id);
        if(existingTedTalk.isPresent())
            return tedTalkMapper.tedTalkToTedTalkResponseDTO(existingTedTalk.get());
        else
            throw new TedTalkNotFoundException(TED_TALK_NOT_FOUND);

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
    }

    public TedTalkResponseDTO updateTedTalk(Long id, TedTalkRequestDTO tedTalkRequestDTO) throws TedTalkNotFoundException{
        try {
            if (tedTalkRepository.existsById(id)) {
                TedTalk newTedTalk = tedTalkMapper.tedTalkRequestToTedTalk(tedTalkRequestDTO);
                newTedTalk.setId(id);
                return tedTalkMapper.tedTalkToTedTalkResponseDTO(tedTalkRepository.save(newTedTalk));
            } else
                throw new TedTalkNotFoundException(TED_TALK_NOT_FOUND);
        } catch (TedTalkNotFoundException e) {
            log.error(e.getMessage(),e);
            throw e;
        }
    }

    public void deleteTedTalk(Long id) throws TedTalkNotFoundException{
            if(tedTalkRepository.existsById(id))
            tedTalkRepository.deleteById(id);
            else
                throw new TedTalkNotFoundException(TED_TALK_NOT_FOUND);
    }

    public Page<TedTalkResponseDTO> searchTedTalks(String author, String title, Long minViews, Long minLikes, Pageable pageable){
        Specification<TedTalk> searchSpec = Specification.where(TedTalkSpecification.setAuthorIfPresent(author))
                .and(TedTalkSpecification.matchTitleIfPresent(title))
                .and(TedTalkSpecification.setMinLikesIfPresent(minLikes))
                .and(TedTalkSpecification.setMinViewsIfPresent(minViews));
        return tedTalkRepository.findAll(searchSpec,pageable).map(tedTalkMapper::tedTalkToTedTalkResponseDTO);
    }

}
