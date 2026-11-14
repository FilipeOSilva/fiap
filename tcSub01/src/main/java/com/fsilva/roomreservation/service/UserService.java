package com.fsilva.roomreservation.service;

import com.fsilva.roomreservation.dto.user.ChangePasswordRequest;
import com.fsilva.roomreservation.dto.user.LoginRequest;
import com.fsilva.roomreservation.dto.user.LoginResponse;
import com.fsilva.roomreservation.dto.user.UserCreateRequest;
import com.fsilva.roomreservation.dto.user.UserResponse;
import com.fsilva.roomreservation.dto.user.UserUpdateRequest;
import java.util.List;

public interface UserService {

    UserResponse create(UserCreateRequest request);

    UserResponse update(Long id, UserUpdateRequest request);

    void changePassword(Long id, ChangePasswordRequest request);

    UserResponse findById(Long id);

    List<UserResponse> searchByName(String name);

    LoginResponse login(LoginRequest request);
}
