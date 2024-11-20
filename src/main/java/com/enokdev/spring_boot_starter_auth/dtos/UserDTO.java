package com.enokdev.spring_boot_starter_auth.dtos;

import java.util.Set;

public record UserDTO(String username, String email, Set<String> roles) {}
