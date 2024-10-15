package org.example.library.mapper;

import org.example.library.dto.MessageDTO;
import org.example.library.model.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MessageMapper {
    MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);


    @Mappings({
            @Mapping(target = "sender", source = "senderId"),
            @Mapping(target = "receiver", source = "receiverId")
    })
    Message toModel(MessageDTO messageDTO);
    MessageDTO toDTO(Message message);
}