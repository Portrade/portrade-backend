package com.linkerbell.portradebackend.domain.user.repository;


import com.linkerbell.portradebackend.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

}
