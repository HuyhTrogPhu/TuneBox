package org.example.library.service.implement;



import org.example.library.dto.UserInformationDto;
import org.example.library.mapper.UserInforMapper;
import org.example.library.model.UserInformation;
import org.example.library.repository.UserInformationRepository;
import org.example.library.service.UserInforService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserInforServiceImplement implements UserInforService {
    @Autowired
    private UserInformationRepository Repo;



    @Override
    public UserInformationDto updateUserInfor(Long userId, UserInformationDto UserInforDTO) {
        Optional<UserInformation> userOptional = Repo.findById(userId);

        if (!userOptional.isPresent()) {
            throw new RuntimeException("User not found with id: " + userId);
        }
        UserInformation userToUpdate = userOptional.get();
        UserInformation updatedUser = UserInforMapper.mapToUserInformation(UserInforDTO);


        updatedUser.setId(userToUpdate.getId()); //ko doi ID
        UserInformation savedUser = Repo.save(updatedUser);
        return UserInforMapper.mapToUserInformationDto(savedUser);
    }

    @Override
    public Optional<UserInformation> findById(Long userId) {
        return Repo.findById(userId);
    }
}
