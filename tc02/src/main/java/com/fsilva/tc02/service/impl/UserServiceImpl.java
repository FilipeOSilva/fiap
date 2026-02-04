package com.fsilva.tc02.service.impl;

import com.fsilva.tc02.dto.ChangePasswordDTO;
import com.fsilva.tc02.dto.CreateUserDTO;
import com.fsilva.tc02.dto.UpdateUserDTO;
import com.fsilva.tc02.domain.user.User;
import com.fsilva.tc02.infra.exceptions.EmailAlreadyExistsException;
import com.fsilva.tc02.infra.exceptions.ResourceNotFoundException;
import com.fsilva.tc02.repository.UserRepository;
import com.fsilva.tc02.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final PasswordEncoder encoder;

    public UserServiceImpl(UserRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    @Override
    public User create(CreateUserDTO dto) {
        if (repository.existsByEmail(dto.getEmail())) {
            throw new EmailAlreadyExistsException();
        }

        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(encoder.encode(dto.getPassword()));

        return repository.save(user);
    }

    @Override
    public User update(String id, UpdateUserDTO dto) {
        User user = repository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());

        return repository.save(user);
    }

    @Override
    public void changePassword(String id, ChangePasswordDTO dto) {
        User user = repository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);

        user.setPassword(encoder.encode(dto.getNewPassword()));
        repository.save(user);
    }

    @Override
    public void delete(String id) {
        repository.deleteById(id);
    }

    @Override
    public List<User> searchByName(String name) {
        return repository.findByNameContainingIgnoreCase(name);

    }
}