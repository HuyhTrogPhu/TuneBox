package org.example.library.mapper;

import org.example.library.dto.EcomAdminDTO;

import org.example.library.model.EcommerceAdmin;
import org.example.library.model.User;

public class EcomAdminMapping {
    public static EcomAdminDTO mapToDTO(EcommerceAdmin admin){
        return new EcomAdminDTO(
                admin.getId(),
                admin.getEmail(),
                admin.getPassword(),
                admin.getName(),
                admin.getGender(),
                admin.getPhoneNumber(),
                admin.getAddress(),
                admin.getAvatar(),
                null
        );
    }
    public static  EcommerceAdmin mapToEntity(EcomAdminDTO admin){
        return new EcommerceAdmin(
                admin.getId(),
                admin.getEmail(),
                admin.getPassword(),
                admin.getName(),
                admin.getGender(),
                admin.getPhoneNumber(),
                admin.getAddress(),
                admin.getAvatar(),
                null
        );
    }
}
