package com.lib.repo;

import com.lib.models.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepoAuthor extends JpaRepository<Author, Long> {
    Author findByName(String name);

}
