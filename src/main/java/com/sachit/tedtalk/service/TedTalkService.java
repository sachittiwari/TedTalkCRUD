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
import java.util.Optional;

@Service
@Slf4j
public class TedTalkService {

    @Autowired
    TedTalkRepository tedTalkRepository;

    @Autowired
    TedTalkMapper tedTalkMapper;

    private static final String TED_TALK_NOT_FOUND = "Ted Talk Not found with the given id";


    /**
     * This method retrieves all the ted talks present in the database
     *
     * @param pageable the details for pagination
     * @return Page of Ted Talks
     */
    public Page<TedTalkResponseDTO> getAllTedTalks(Pageable pageable){
                return tedTalkRepository.findAll(pageable)
                        .map(tedTalkMapper::tedTalkToTedTalkResponseDTO);
    }

    /**
     * This method retrieves the specific ted talk based on the id provided
     *
     * @param id of the ted talk
     * @return The Ted Talk Response DTO object providing the ted talk details
     * @throws TedTalkNotFoundException
     */
    public TedTalkResponseDTO getTedTalk(Long id) throws TedTalkNotFoundException{
        Optional<TedTalk> existingTedTalk = tedTalkRepository.findById(id);
        if(!existingTedTalk.isPresent())
            throw new TedTalkNotFoundException(TED_TALK_NOT_FOUND);
        return tedTalkMapper.tedTalkToTedTalkResponseDTO(existingTedTalk.get());
    }

    /**
     * This method creates a new Ted Talk based on the details provided
     *
     * @param tedTalkRequestDTO the details of the new ted talk
     * @return The Ted Talk Response DTO object of the newly created ted talk
     * @throws TedTalkExistsException
     */
    public TedTalkResponseDTO createTedTalk(TedTalkRequestDTO tedTalkRequestDTO) throws TedTalkExistsException {
            Optional<TedTalk> existingTedTalk = tedTalkRepository.findByTitle(tedTalkRequestDTO.getTitle());
            if(existingTedTalk.isPresent())
                throw new TedTalkExistsException("Ted Talk with the same title already exists");
            return tedTalkMapper.tedTalkToTedTalkResponseDTO(tedTalkRepository.save(tedTalkMapper.tedTalkRequestToTedTalk(tedTalkRequestDTO)));
    }

    /**
     * This method updated the existing ted talk details
     *
     * @param id of the ted talk to be updated
     * @param tedTalkRequestDTO the details of the updated ted talk
     * @return Updated Ted Talk Response object
     * @throws TedTalkNotFoundException
     */
    public TedTalkResponseDTO updateTedTalk(Long id, TedTalkRequestDTO tedTalkRequestDTO) throws TedTalkNotFoundException{
            if (tedTalkRepository.existsById(id)) {
                TedTalk newTedTalk = tedTalkMapper.tedTalkRequestToTedTalk(tedTalkRequestDTO);
                newTedTalk.setId(id);
                return tedTalkMapper.tedTalkToTedTalkResponseDTO(tedTalkRepository.save(newTedTalk));
            } else
                throw new TedTalkNotFoundException(TED_TALK_NOT_FOUND);

    }

    /**
     * This method deletes the ted talk based on the provided id
     *
     * @param id of the ted talk to be deleted
     * @throws TedTalkNotFoundException
     */
    public void deleteTedTalk(Long id) throws TedTalkNotFoundException{
            if(tedTalkRepository.existsById(id))
            tedTalkRepository.deleteById(id);
            else
                throw new TedTalkNotFoundException(TED_TALK_NOT_FOUND);
    }

    /**
     * This method searches the ted talk based on the search criteria
     *
     * @param author of the Ted Talk
     * @param title of the Ted Talk
     * @param minViews the minimum number of views for any ted talk
     * @param minLikes the minimum number of likes for any ted talk
     * @param pageable pageable the details for pagination
     * @return Page of Ted Talks
     */
    public Page<TedTalkResponseDTO> searchTedTalks(String author, String title, Long minViews, Long minLikes, Pageable pageable){
        Specification<TedTalk> searchSpec = Specification.where(TedTalkSpecification.setAuthorIfPresent(author))
                .and(TedTalkSpecification.matchTitleIfPresent(title))
                .and(TedTalkSpecification.setMinLikesIfPresent(minLikes))
                .and(TedTalkSpecification.setMinViewsIfPresent(minViews));
        return tedTalkRepository.findAll(searchSpec,pageable).map(tedTalkMapper::tedTalkToTedTalkResponseDTO);
    }

}
