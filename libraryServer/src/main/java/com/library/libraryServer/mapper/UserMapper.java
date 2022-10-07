package com.library.libraryServer.mapper;

import com.fasterxml.jackson.databind.*;
import com.library.libraryServer.domain.*;
import com.library.libraryServer.domain.dto.*;
import org.springframework.stereotype.*;

@Service
public class UserMapper {
    private final ObjectMapper objectMapper = new ObjectMapper();
    public UserDTO toDTO(User user){
        if(user==null){
            return null;
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPlan(user.getPlan());
        userDTO.setAuthority(user.getAuthority());
        userDTO.setAccount(user.getAccount());
        userDTO.setAccountStatus(user.getAccountStatus());
        userDTO.setLastBillingDate(user.getLastBillingDate());
        userDTO.setNextBillingDate(user.getNextBillingDate());
        userDTO.setPassword(user.getPassword());
        userDTO.setPhoneNumber(user.getPhoneNumber());




        return userDTO;
    }
}
