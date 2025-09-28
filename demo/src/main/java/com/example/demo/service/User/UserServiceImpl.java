package com.example.demo.service.User;

import com.example.demo.constant.PredefinedRole;
import com.example.demo.dto.request.ProfileCreationRequest;
import com.example.demo.dto.request.UserCreationRequest;
import com.example.demo.dto.request.UserUpdateRequest;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.mapper.ProfileMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.httpclient.ProfileClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    RoleRepository roleRepository;
    ProfileClient profileClient;
    ProfileMapper profileMapper;

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public List<UserResponse> getAllUsers() {
        log.info("Getting all users");
        List<User> users = userRepository.findAll();

        return users.stream().map(userMapper::toUserResponse).toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public UserResponse getUserById(String id) {
        return userMapper.toUserResponse(userRepository.findById(id).orElseThrow(()-> new RuntimeException("User not found")));
    }

    @Override
    public UserResponse getMyInfo() {
        SecurityContext context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepository.findByUsername(name).orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTED));
        return userMapper.toUserResponse(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public void deleteUser(String id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }

    @Override
    public UserResponse CreateUser(UserCreationRequest reg) {
        // Check tồn tại
        if (userRepository.existsByUsername(reg.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        User user = userMapper.toUser(reg);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(reg.getPassword()));
        HashSet<Role> roles = new HashSet<>();
        roleRepository.findById(PredefinedRole.USER_ROLE).ifPresent(roles::add);
        user.setRoles(roles);
        userRepository.save(user);
        ProfileCreationRequest ProfileRequest = profileMapper.toProfileCreationRequest(reg);
        ProfileRequest.setUserId(user.getId());
        var profileReponse = profileClient.creationProfile(ProfileRequest);
        log.info(profileReponse.toString());
        UserResponse userResponse = userMapper.toUserResponse(user);
        userResponse.setFirstName(profileReponse.getFirstName());
        userResponse.setLastName(profileReponse.getLastName());
        userResponse.setCity(profileReponse.getCity());
        userResponse.setBirthDate(profileReponse.getBirthDate());
        return userResponse;
    }

    @Override
    public UserResponse UpdateUser(String id,UserUpdateRequest reg) {
        User user = userRepository.findById(id).orElseThrow(()-> new RuntimeException("User not found"));
       userMapper.updateUser(user, reg);

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(reg.getPassword()));

        var roles = roleRepository.findAllById(reg.getRoles());
        user.setRoles(new HashSet<>(roles));
       // user.setRole(roles);

        return userMapper.toUserResponse(userRepository.save(user));
    }
}
