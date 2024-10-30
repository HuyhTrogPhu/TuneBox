package org.example.library.mapper;


import org.example.library.dto.UserInformationDto;
import org.example.library.model.UserInformation;

public class UserInforMapper {
    public static UserInformationDto mapToUserInformationDto(UserInformation userInformation) {
        if (userInformation == null) {
            return null;
        }

        return new UserInformationDto(
                userInformation.getId(),
                userInformation.getName(),
                userInformation.getGender(),
                userInformation.getPhoneNumber(),
                userInformation.getBirthDay(),
                userInformation.getAvatar(),
                userInformation.getBackground(),
                userInformation.getAbout()
        );
    }

    public static UserInformation mapToUserInformation(UserInformationDto userInformationDto) {
        if (userInformationDto == null) {
            return null;
        }

        UserInformation userInformation = new UserInformation();
        userInformation.setId(userInformationDto.getUserId());
        userInformation.setName(userInformationDto.getName());
        userInformation.setGender(userInformationDto.getGender());
        userInformation.setPhoneNumber(userInformationDto.getPhoneNumber());
        userInformation.setBirthDay(userInformationDto.getBirthDay());
        userInformation.setAvatar(userInformationDto.getAvatar());
        userInformation.setBackground(userInformationDto.getBackground());
        userInformation.setAbout(userInformationDto.getAbout());

        return userInformation;
    }
}
