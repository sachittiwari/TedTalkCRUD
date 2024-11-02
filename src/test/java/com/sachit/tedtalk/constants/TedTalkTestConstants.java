package com.sachit.tedtalk.constants;

import com.sachit.tedtalk.entity.TedTalk;
import com.sachit.tedtalk.model.TedTalkRequestDTO;
import com.sachit.tedtalk.model.TedTalkResponseDTO;

public class TedTalkTestConstants {
    public static final TedTalkResponseDTO TED_TALK_1 = new TedTalkResponseDTO(1L,"TedTalk 1","Sachit","01-2024",100L,500L,"www.tedtalk1.com");
    public static final TedTalkResponseDTO TED_TALK_2 = new TedTalkResponseDTO(2L,"TedTalk 2","Tiwari","02-2024",200L,700L,"www.tedtalk2.com");
    public static final String TED_TALK_NOT_FOUND = "Ted Talk Not found with the given id";
    public static final String TED_TALK_EXISTS = "Ted Talk with the same title already exists";
    public static final TedTalkRequestDTO NEW_TED_TALK = new TedTalkRequestDTO("New ted Talk","New Author","09-2024",500L,600L,"www.newtedtalk.com");

    public static final TedTalk TED_TALK_ENTITY_1 = new TedTalk(1L,"TedTalk 1","Sachit","01-2024",100L,500L,"www.tedtalk1.com");
    public static final TedTalk TED_TALK_ENTITY_2 = new TedTalk(1L,"TedTalk 2","Sachit","02-2024",200L,700L,"www.tedtalk2.com");
    //public static final Page<TedTalkResponseDTO> tedTalkList= new PageImpl<>();
}
