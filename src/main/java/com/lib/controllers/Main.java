package com.lib.controllers;

import com.lib.models.Books;
import com.lib.models.Users;
import com.lib.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public class Main {

    @Autowired
    RepoUsers repoUsers;

    @Autowired
    RepoBooks repoBooks;

    @Autowired
    RepoComments repoComments;

    @Autowired
    RepoBookIncome repoBookIncome;

    @Autowired
    RepoAuthor repoAuthor;

    @Value("${upload.path}")
    String uploadPath;


    String checkUserRole() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if ((!(auth instanceof AnonymousAuthenticationToken)) && auth != null) {
            UserDetails userDetail = (UserDetails) auth.getPrincipal();
            if (userDetail != null) {
                Users userFromDB = repoUsers.findByUsername(userDetail.getUsername());
                return String.valueOf(userFromDB.getRole());
            }
            return "NOT";
        }
        return "NOT";
    }

    long returnLastGameId() {
        List<Books> booksList = repoBooks.findAll();
        return booksList.get(booksList.size() - 1).getId();
    }
}
