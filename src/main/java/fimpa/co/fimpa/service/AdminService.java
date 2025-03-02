package fimpa.co.fimpa.service;

import fimpa.co.fimpa.model.Admin;
import fimpa.co.fimpa.model.Role;
import fimpa.co.fimpa.repository.AdminRepository;
import fimpa.co.fimpa.repository.RoleRepository;
import fimpa.co.fimpa.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${file.upload-dir:D:/uploads/}")
    private String uploadDirectory;

    public void register(Admin admin) {
        if (adminRepository.findByEmail(admin.getEmail()).isPresent()
                || usersRepository.findByEmail(admin.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        Role adminRole = roleRepository.findByName("ADMIN").orElseThrow(() -> new RuntimeException("Role not found"));
        admin.setRole(adminRole);
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        adminRepository.save(admin);
    }

    public String saveImage(MultipartFile image, String folder) throws IOException {
        String uploadDir = uploadDirectory + folder;
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String imageName = image.getOriginalFilename();
        File imageFile = new File(uploadDir + imageName);
        image.transferTo(imageFile);
        return "/uploads/" + folder + imageName;
    }

    public Admin loginByEmail(String email, String password) {
        Optional<Admin> admin = adminRepository.findByEmail(email);
        return admin.filter(a -> passwordEncoder.matches(password, a.getPassword())).orElse(null);
    }

    public Admin findByEmail(String email) {
        return adminRepository.findByEmail(email).orElse(null);
    }

    public List<Admin> findAll() {
        return adminRepository.findAll();
    }
}