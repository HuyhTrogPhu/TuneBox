package org.example.library.service;


import org.example.library.dto.EcomAdminDTO;

public interface EcomAdminService {

    public EcomAdminDTO login(EcomAdminDTO admin);

    public EcomAdminDTO AddAdmin(EcomAdminDTO admin);
}
