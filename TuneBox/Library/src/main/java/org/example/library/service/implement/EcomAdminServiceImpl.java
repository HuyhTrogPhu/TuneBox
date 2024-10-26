//package org.example.library.service.implement;
//
//
//import lombok.AllArgsConstructor;
//
//
//import org.example.library.dto.EcomAdminDTO;
//import org.example.library.mapper.EcomAdminMapping;
//import org.example.library.model.EcommerceAdmin;
//import org.example.library.repository.*;
//import org.example.library.service.EcomAdminService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.web.server.ResponseStatusException;
//
//
//import java.util.*;
//
//@Service
////@AllArgsConstructor
//public class EcomAdminServiceImpl implements EcomAdminService {
//    private final EcommerceAdminRepository Repo;
//
//    private final RoleRepository roleRepo;
//
//    private final PasswordEncoder passwordEncoder; // Thay đổi kiểu biến ở đây
//
//    @Autowired
//    public EcomAdminServiceImpl(PasswordEncoder passwordEncoder,
//                                EcommerceAdminRepository Repo,
//                                RoleRepository roleRepo) {
//        this.Repo = Repo;
//        this.roleRepo = roleRepo;
//        this.passwordEncoder = passwordEncoder;
//    }
//
//
//    @Override
//    public EcomAdminDTO login(EcomAdminDTO admin) {
//        Optional<EcommerceAdmin> ecommerceAdmin;
//
//        if (admin.getEmail() == null || admin.getEmail().isEmpty()) {
//            throw new RuntimeException("email khong duoc de trong");
//        }
//
//        ecommerceAdmin = Repo.findByEmail(admin.getEmail());
//
//        if (ecommerceAdmin.isPresent()) {
//            EcommerceAdmin adminEntity = ecommerceAdmin.get();
//
////            // check role {ECOMADMIN}
////            boolean hasAdminRole = adminEntity.getRole().stream()
////                    .anyMatch(role -> role.getName().equals("ECOMADMIN"));
////
////            if (!hasAdminRole) {
////                throw new RuntimeException("ACCESS DENIED");
////            }
//
//
//            if (passwordEncoder.matches(admin.getPassword(), adminEntity.getPassword())) {
//                return EcomAdminMapping.mapToDTO(adminEntity);
//            } else {
//                throw new RuntimeException("sai username/password");
//            }
//        } else {
//            throw new RuntimeException("Khong tim thay nguoi dung");
//        }
//    }
//
//
//    @Override
//    public EcomAdminDTO AddAdmin(EcomAdminDTO admin){
//        List<EcommerceAdmin> fullList = Repo.findAll();
//        for (EcommerceAdmin check : fullList) {
//            if (check.getEmail().equals(admin.getEmail())) {
//                System.out.println("trùng Email: " + admin.getEmail());
//                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email đã tồn tại");
//            }}
//        try {
//            EcommerceAdmin adminSaved = EcomAdminMapping.mapToEntity(admin);
//            adminSaved.setRole(roleRepo.findByName("ECOMADMIN"));
//            adminSaved.setPassword(passwordEncoder.encode(admin.getPassword()));
//            Repo.save(adminSaved);
//            return EcomAdminMapping.mapToDTO(adminSaved);
//        }catch(Exception e){
//            e.printStackTrace();
//            return null;
//        }
//    }
//}