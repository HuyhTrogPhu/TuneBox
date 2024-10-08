package org.example.library.service.implement;


import lombok.AllArgsConstructor;


import org.example.library.dto.EcomAdminDTO;
import org.example.library.dto.UserDto;
import org.example.library.mapper.EcomAdminMapping;
import org.example.library.model.EcommerceAdmin;
import org.example.library.model.User;
import org.example.library.repository.*;
import org.example.library.service.EcomAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
@AllArgsConstructor
public class EcomAdminServiceImpl implements EcomAdminService {
    @Autowired
    private EcommerceAdminRepository Repo;

    @Autowired
    private RoleRepository roleRepo;

    @Override
    public EcomAdminDTO login(EcomAdminDTO admin) {
        Optional<EcommerceAdmin> ecommerceAdmin;

        if (admin.getEmail() == null || admin.getEmail().isEmpty()) {
            throw new RuntimeException("Username or email cannot be null or empty");
        }

        ecommerceAdmin = Repo.findByEmail(admin.getEmail());

        if (ecommerceAdmin.isPresent()) {
            EcommerceAdmin adminEntity = ecommerceAdmin.get();

            // check role {ECOMADMIN}
            boolean hasAdminRole = adminEntity.getRole().stream()
                    .anyMatch(role -> role.getName().equals("ECOMADMIN"));

            if (!hasAdminRole) {
                throw new RuntimeException("ACCESS DENIED");
            }

            // Kiểm tra mật khẩu (nên dùng so sánh bảo mật nếu mật khẩu được mã hóa)
            if (admin.getPassword().equals(adminEntity.getPassword())) {
                return EcomAdminMapping.mapToDTO(adminEntity);
            } else {
                throw new RuntimeException("Invalid username or password");
            }
        } else {
            throw new RuntimeException("Invalid username or password");
        }
    }


    @Override
    public EcomAdminDTO AddAdmin(EcomAdminDTO admin){
try {
    EcommerceAdmin adminSaved = new EcommerceAdmin();
    adminSaved.setRole(roleRepo.findByName("ECOMADMIN"));
    Repo.save(EcomAdminMapping.mapToEntity(admin));
    return EcomAdminMapping.mapToDTO(adminSaved);
}catch (Exception e){
    e.printStackTrace();
    return null;
}
    }
}
