package fimpa.co.fimpa.service;

import fimpa.co.fimpa.model.Admin;
import fimpa.co.fimpa.model.Role;
import fimpa.co.fimpa.repository.AdminRepository;
import fimpa.co.fimpa.repository.UsersRepository; // Tambahkan repository users
import fimpa.co.fimpa.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UsersRepository usersRepository; // Tambahkan repository users

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void register(Admin admin) {
        // Periksa apakah email sudah terdaftar sebagai user atau admin
        if (adminRepository.findByEmail(admin.getEmail()).isPresent() || usersRepository.findByEmail(admin.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        Role adminRole = roleRepository.findByName("ADMIN").orElseThrow(() -> new RuntimeException("Role not found"));
        admin.setRole(adminRole);
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        adminRepository.save(admin);
    }

    public Admin loginByEmail(String email, String password) {
        Optional<Admin> admin = adminRepository.findByEmail(email);
        return admin.filter(a -> passwordEncoder.matches(password, a.getPassword())).orElse(null);
    }
}