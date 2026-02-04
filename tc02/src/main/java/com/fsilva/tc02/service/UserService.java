package com.fsilva.tc02.service;


import com.fsilva.tc02.dto.ChangePasswordDTO;
import com.fsilva.tc02.dto.CreateUserDTO;
import com.fsilva.tc02.dto.UpdateUserDTO;
import com.fsilva.tc02.domain.user.User;

import java.util.List;

public interface UserService {

        User create(CreateUserDTO dto);
        User update(String id, UpdateUserDTO dto);
        void delete(String id);
        void changePassword(String id, ChangePasswordDTO dto);
        List<User> searchByName(String name);
}
