package com.sachit.tedtalk.model;

import com.sachit.tedtalk.entity.TedTalk;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public interface TedTalkMapper {

    TedTalkResponseDTO tedTalkToTedTalkResponseDTO(TedTalk tedTalk);
    TedTalk tedTalkRequestToTedTalk(TedTalkRequestDTO tedTalkRequestDTO);

}
