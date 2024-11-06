package org.example.library.mapper;

import org.example.library.dto.*;
import org.example.library.model.Message;
import org.example.library.model.OtherAttachment;
import org.example.library.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ChatMessageMapper {

    @Mappings({
            @Mapping(target = "sender.id", source = "senderId.id"),
            @Mapping(target = "receiver.id", source = "receiverId.id"),
            @Mapping(target = "dateTime", source = "creationDate")
    })
    Message toModel(MessageWebSocketDTO messageWebSocketDTO);

    @Mappings({
            @Mapping(source = "sender.id", target = "senderId.id"),
            @Mapping(source = "receiver.id", target = "receiverId.id"),
            @Mapping(source = "dateTime", target = "creationDate")
    })
    MessageWebSocketDTO toDto(Message message);

    // Các ánh xạ khác cho OtherAttachment
    OtherAttachment attachmentDtoToModel(OtherAttachmentDto dto);
    OtherAttachmentDto attachmentModelToDto(OtherAttachment attachment);

    default User map(UserMessageDTO userMessageDTO) {
        if (userMessageDTO == null) {
            return null;
        }
        User user = new User();
        user.setId(userMessageDTO.getId());
        // Ánh xạ các trường khác nếu cần
        return user;
    }

}