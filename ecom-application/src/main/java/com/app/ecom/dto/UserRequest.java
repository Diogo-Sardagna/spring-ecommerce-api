package com.app.ecom.dto;

import lombok.Data;

@Data
public class UserRequest {
    private String lastName;
    private String firstName;
    private String email;
    private String phone;
    private AddressDTO address;
}
