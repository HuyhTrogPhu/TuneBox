package org.example.library.mapper;


import org.example.library.dto.UserInforDTO;
import org.example.library.model.UserInformation;

public class UserInforMapper {
    public static UserInforDTO maptoDto(UserInformation User) {
        return new UserInforDTO(
                User.getId(),
                User.getFirstName(),
                User.getGender(),
                User.getPhoneNumber(),
                User.getBirthDate(),
                User.getAvatar(),
                User.getBackground(),
                User.getAbout(),
                User.getNumber(),
                User.getUser()
        ); }
        public static UserInformation maptoEntity(UserInforDTO User) {
            return new UserInformation(
                    User.getId(),
                    User.getFirstName(),
                    User.getGender(),
                    User.getPhoneNumber(),
                    User.getBirthDate(),
                    User.getAvatar(),
                    User.getBackground(),
                    User.getAbout(),
                    User.getNumber(),
                    User.getUser()
            );
        }
}
