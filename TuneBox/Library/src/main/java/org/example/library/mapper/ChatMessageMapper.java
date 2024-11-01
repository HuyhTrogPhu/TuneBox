package org.example.library.mapper;

import org.example.library.dto.MessageWebSocketDTO;
import org.example.library.dto.UserMessageDTO;
import org.example.library.model.Message;
import org.example.library.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChatMessageMapper {
//    MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);

//    @Mapping(target = "sender.id", source = "senderId")
//    @Mapping(target = "receiver.id", source = "receiverId")
//    @Mapping(target = "dateTime", source = "creationDate")
//    Message toModel(MessageDTO messageDTO);
//


//    @Mapping(target = "senderId", source = "sender.id")
//    @Mapping(target = "receiverId", source = "receiver.id")
//    @Mapping(target = "creationDate", source = "dateTime")
//    MessageDTO toDTO(Message message);

    // Thêm phương thức để chuyển đổi từ MessageWebSocketDTO sang Message
    @Mapping(target = "sender.id", source = "senderId.id")
    @Mapping(target = "receiver.id", source = "receiverId.id")
    @Mapping(target = "dateTime", source = "creationDate")
    Message toModel(MessageWebSocketDTO messageWebSocketDTO);

    default User map(UserMessageDTO userMessageDTO) {
        if (userMessageDTO == null) {
            return null;
        }
        User user = new User();
        user.setId(userMessageDTO.getId());
        // Ánh xạ các trường khác nếu cần
        return user;
    }

//    default UserDto map(User user) {
//        if (user == null) {
//            return null;
//        }
//        UserDto userDto = new UserDto();
//        userDto.setId(user.getId());
//        // Ánh xạ các trường khác nếu cần
//        return userDto;
//    }
}