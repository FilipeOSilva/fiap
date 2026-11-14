package com.fsilva.roomreservation.service.impl;

import com.fsilva.roomreservation.domain.user.User;
import com.fsilva.roomreservation.domain.user.UserRole;
import com.fsilva.roomreservation.dto.user.ChangePasswordRequest;
import com.fsilva.roomreservation.dto.user.LoginRequest;
import com.fsilva.roomreservation.dto.user.LoginResponse;
import com.fsilva.roomreservation.dto.user.UserCreateRequest;
import com.fsilva.roomreservation.dto.user.UserResponse;
import com.fsilva.roomreservation.dto.user.UserUpdateRequest;
import com.fsilva.roomreservation.infra.exceptions.BusinessException;
import com.fsilva.roomreservation.infra.exceptions.ConflictException;
import com.fsilva.roomreservation.infra.exceptions.ResourceNotFoundException;
import com.fsilva.roomreservation.infra.exceptions.UnauthorizedException;
import com.fsilva.roomreservation.mapper.UserMapper;
import com.fsilva.roomreservation.repository.UserRepository;
import com.fsilva.roomreservation.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserResponse create(UserCreateRequest request) {
        String email = normalizeEmail(request.getEmail());
        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new ConflictException("E-mail já cadastrado");
        }

        User user = new User();
        user.setName(request.getName().trim());
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole() == null ? UserRole.USUARIO : request.getRole());

        try {
            user = userRepository.saveAndFlush(user);
        } catch (DataIntegrityViolationException exception) {
            throw new ConflictException("E-mail já cadastrado");
        }
        return userMapper.toResponse(user);
    }

    @Override
    @Transactional
    public UserResponse update(Long id, UserUpdateRequest request) {
        User user = getEntity(id);
        String email = normalizeEmail(request.getEmail());

        if (userRepository.existsByEmailIgnoreCaseAndIdNot(email, id)) {
            throw new ConflictException("E-mail já cadastrado");
        }

        user.setName(request.getName().trim());
        user.setEmail(email);
        try {
            user = userRepository.saveAndFlush(user);
        } catch (DataIntegrityViolationException exception) {
            throw new ConflictException("E-mail já cadastrado");
        }
        return userMapper.toResponse(user);
    }

    @Override
    @Transactional
    public void changePassword(Long id, ChangePasswordRequest request) {
        User user = getEntity(id);
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new BusinessException("Senha atual inválida");
        }
        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new BusinessException("A nova senha deve ser diferente da senha atual");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse findById(Long id) {
        return userMapper.toResponse(getEntity(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> searchByName(String name) {
        String term = name == null ? "" : name.trim();
        return userRepository.findByNameContainingIgnoreCaseOrderByNameAsc(term).stream()
                .map(userMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmailIgnoreCase(normalizeEmail(request.getEmail()))
                .orElseThrow(() -> new UnauthorizedException("Credenciais inválidas"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Credenciais inválidas");
        }
        return userMapper.toLoginResponse(user);
    }

    private User getEntity(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado: " + id));
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase();
    }
}
