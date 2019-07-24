package com.ethanaa.photo.security.service;


import com.ethanaa.photo.security.entity.User;

import java.util.Optional;

public interface UserLookupService {

    Optional<User> getByUsername(String username);
}
