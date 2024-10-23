package org.ga.chess.service;

import lombok.Setter;
import org.ga.chess.model.Admin;
import org.ga.chess.repository.IAdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Setter
public class AdminService {
    @Autowired
    private IAdminRepository adminRepository;


}
