package fimpa.co.fimpa.service;

import fimpa.co.fimpa.model.Users;
import fimpa.co.fimpa.model.Role;
import fimpa.co.fimpa.repository.UsersRepository;
import fimpa.co.fimpa.repository.AdminRepository;
import fimpa.co.fimpa.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsersService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private AdminRepository adminRepository; // Tambahkan repository admin

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void register(Users user) {
        // Periksa apakah email sudah terdaftar sebagai user atau admin
        if (usersRepository.findByEmail(user.getEmail()).isPresent() || adminRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        Role userRole = roleRepository.findByName("USER").orElseThrow(() -> new RuntimeException("Role not found"));
        user.setRole(userRole);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        usersRepository.save(user);
    }

    public Users loginByEmail(String email, String password) {
        Optional<Users> user = usersRepository.findByEmail(email);
        return user.filter(u -> passwordEncoder.matches(password, u.getPassword())).orElse(null);
    }
}