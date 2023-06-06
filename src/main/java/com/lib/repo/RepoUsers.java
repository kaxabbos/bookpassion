package com.lib.repo;

import com.lib.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepoUsers extends JpaRepository<Users, Long> {
    Users findByUsername(String username);
}
