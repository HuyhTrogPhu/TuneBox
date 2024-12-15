package org.example.library.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.example.library.dto.MessageWebSocketDTO;
import org.example.library.dto.OtherAttachmentDto;
import org.example.library.dto.UserMessageDTO;
import org.example.library.model.Message;
import org.example.library.model.OtherAttachment;
import org.example.library.model.User;
import org.example.library.model_enum.MessageStatus;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-14T20:42:10+0700",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 21.0.4 (Oracle Corporation)"
)
@Component
public class ChatMessageMapperImpl implements ChatMessageMapper {

    @Override
    public Message toModel(MessageWebSocketDTO messageWebSocketDTO) {
        if ( messageWebSocketDTO == null ) {
            return null;
        }

        Message message = new Message();

        message.setSender( userMessageDTOToUser( messageWebSocketDTO.getSenderId() ) );
        message.setReceiver( userMessageDTOToUser1( messageWebSocketDTO.getReceiverId() ) );
        message.setDateTime( messageWebSocketDTO.getCreationDate() );
        message.setId( messageWebSocketDTO.getId() );
        message.setContent( messageWebSocketDTO.getContent() );
        message.setAttachments( otherAttachmentDtoListToOtherAttachmentList( messageWebSocketDTO.getAttachments() ) );
        if ( messageWebSocketDTO.getStatus() != null ) {
            message.setStatus( Enum.valueOf( MessageStatus.class, messageWebSocketDTO.getStatus() ) );
        }

        return message;
    }

    @Override
    public MessageWebSocketDTO toDto(Message message) {
        if ( message == null ) {
            return null;
        }

        MessageWebSocketDTO messageWebSocketDTO = new MessageWebSocketDTO();

        messageWebSocketDTO.setSenderId( userToUserMessageDTO( message.getSender() ) );
        messageWebSocketDTO.setReceiverId( userToUserMessageDTO1( message.getReceiver() ) );
        messageWebSocketDTO.setCreationDate( message.getDateTime() );
        messageWebSocketDTO.setId( message.getId() );
        messageWebSocketDTO.setContent( message.getContent() );
        if ( message.getStatus() != null ) {
            messageWebSocketDTO.setStatus( message.getStatus().name() );
        }
        messageWebSocketDTO.setAttachments( otherAttachmentListToOtherAttachmentDtoList( message.getAttachments() ) );

        return messageWebSocketDTO;
    }

    @Override
    public OtherAttachment attachmentDtoToModel(OtherAttachmentDto dto) {
        if ( dto == null ) {
            return null;
        }

        OtherAttachment otherAttachment = new OtherAttachment();

        otherAttachment.setId( dto.getId() );
        otherAttachment.setFileName( dto.getFileName() );
        otherAttachment.setFileUrl( dto.getFileUrl() );
        otherAttachment.setMimeType( dto.getMimeType() );
        otherAttachment.setContentType( dto.getContentType() );
        otherAttachment.setSize( dto.getSize() );

        return otherAttachment;
    }

    @Override
    public OtherAttachmentDto attachmentModelToDto(OtherAttachment attachment) {
        if ( attachment == null ) {
            return null;
        }

        OtherAttachmentDto otherAttachmentDto = new OtherAttachmentDto();

        otherAttachmentDto.setId( attachment.getId() );
        otherAttachmentDto.setFileName( attachment.getFileName() );
        otherAttachmentDto.setFileUrl( attachment.getFileUrl() );
        otherAttachmentDto.setMimeType( attachment.getMimeType() );
        otherAttachmentDto.setContentType( attachment.getContentType() );
        otherAttachmentDto.setSize( attachment.getSize() );

        return otherAttachmentDto;
    }

    protected User userMessageDTOToUser(UserMessageDTO userMessageDTO) {
        if ( userMessageDTO == null ) {
            return null;
        }

        User user = new User();

        user.setId( userMessageDTO.getId() );

        return user;
    }

    protected User userMessageDTOToUser1(UserMessageDTO userMessageDTO) {
        if ( userMessageDTO == null ) {
            return null;
        }

        User user = new User();

        user.setId( userMessageDTO.getId() );

        return user;
    }

    protected List<OtherAttachment> otherAttachmentDtoListToOtherAttachmentList(List<OtherAttachmentDto> list) {
        if ( list == null ) {
            return null;
        }

        List<OtherAttachment> list1 = new ArrayList<OtherAttachment>( list.size() );
        for ( OtherAttachmentDto otherAttachmentDto : list ) {
            list1.add( attachmentDtoToModel( otherAttachmentDto ) );
        }

        return list1;
    }

    protected UserMessageDTO userToUserMessageDTO(User user) {
        if ( user == null ) {
            return null;
        }

        UserMessageDTO userMessageDTO = new UserMessageDTO();

        userMessageDTO.setId( user.getId() );

        return userMessageDTO;
    }

    protected UserMessageDTO userToUserMessageDTO1(User user) {
        if ( user == null ) {
            return null;
        }

        UserMessageDTO userMessageDTO = new UserMessageDTO();

        userMessageDTO.setId( user.getId() );

        return userMessageDTO;
    }

    protected List<OtherAttachmentDto> otherAttachmentListToOtherAttachmentDtoList(List<OtherAttachment> list) {
        if ( list == null ) {
            return null;
        }

        List<OtherAttachmentDto> list1 = new ArrayList<OtherAttachmentDto>( list.size() );
        for ( OtherAttachment otherAttachment : list ) {
            list1.add( attachmentModelToDto( otherAttachment ) );
        }

        return list1;
    }
}
