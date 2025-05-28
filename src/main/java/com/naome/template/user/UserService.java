package com.naome.template.user;

import com.naome.template.auth.dtos.RegisterRequestDTO;
import com.naome.template.commons.exceptions.BadRequestException;
import com.naome.template.user.dtos.UserResponseDTO;
import com.naome.template.user.mappers.UserMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDTO createUser(RegisterRequestDTO user){
        if(userRepository.existsByEmailOrNationalIdOrPhoneNumber(user.email(), user.nationalId(),user.phoneNumber())){
            throw new BadRequestException("User with this email or nationalId or phoneNumber already exists");
        }

        var newUser = userMapper.toEntity(user);
        newUser.setPassword(passwordEncoder.encode(user.password()));
        newUser.setRole(Role.USER);
        newUser.setEnabled(false);
        log.info("User created: {}", newUser);
        userRepository.save(newUser);
        return userMapper.toResponseDTO(newUser);

    }

    public void changeUserPassword(String userEmail, String newPassword){
        var user = findByEmail(userEmail);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public void activateUserAccount(String userEmail){
        var user = findByEmail(userEmail);
        user.setEnabled(true);
        userRepository.save(user);
    }

    public User findByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(() -> new BadRequestException("User with that email not found."));
    }
}
