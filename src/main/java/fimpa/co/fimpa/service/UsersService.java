package fimpa.co.fimpa.service;

import fimpa.co.fimpa.model.Users;
import fimpa.co.fimpa.model.Role;
import fimpa.co.fimpa.repository.UsersRepository;
import fimpa.co.fimpa.repository.AdminRepository;
import fimpa.co.fimpa.repository.RoleRepository;
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
public class UsersService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${file.upload-dir:D:/uploads/}")
    private String uploadDirectory;

    public void register(Users user) {
        if (usersRepository.findByEmail(user.getEmail()).isPresent()
                || adminRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        Role userRole = roleRepository.findByName("USER").orElseThrow(() -> new RuntimeException("Role not found"));
        user.setRole(userRole);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        usersRepository.save(user);
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

    public Users loginByEmail(String email, String password) {
        Optional<Users> user = usersRepository.findByEmail(email);
        return user.filter(u -> passwordEncoder.matches(password, u.getPassword())).orElse(null);
    }

    public Users findByEmail(String email) {
        return usersRepository.findByEmail(email).orElse(null);
    }

    public List<Users> findAll() {
        return usersRepository.findAll();
    }

    public boolean emailExists(String email) {
        return usersRepository.existsByEmail(email);
    }
}