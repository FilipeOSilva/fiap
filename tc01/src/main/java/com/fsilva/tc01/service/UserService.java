package com.fsilva.tc01.service;


import com.fsilva.tc01.dto.ChangePasswordDTO;
import com.fsilva.tc01.dto.CreateUserDTO;
import com.fsilva.tc01.dto.UpdateUserDTO;
import com.fsilva.tc01.domain.user.User;

import java.util.List;
import java.util.UUID;

public interface UserService {

        User create(CreateUserDTO dto);
        User update(String id, UpdateUserDTO dto);
        void delete(String id);
        void changePassword(String id, ChangePasswordDTO dto);
        List<User> searchByName(String name);
}
