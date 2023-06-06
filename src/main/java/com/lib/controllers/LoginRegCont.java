package com.lib.controllers;

import com.lib.models.enums.Role;
import com.lib.models.Users;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginRegCont extends Main {

    /*  login   */

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("role", checkUserRole());
        return "login";
    }

    /*  reg   */

    @GetMapping("/reg")
    public String reg(Model model) {
        model.addAttribute("role", checkUserRole());
        return "reg";
    }

    @PostMapping("/reg")
    public String addUser(Users user, Model model) {
        if (repoUsers.findAll().size() == 0 || repoUsers.findAll().isEmpty()) {
            user.setRole(Role.ADMIN);
            repoUsers.save(user);
            return "redirect:/login";
        }
        Users userFromDB = repoUsers.findByUsername(user.getUsername());
        if (userFromDB != null) {
            model.addAttribute("role", checkUserRole());
            model.addAttribute("message", "Пользователь c таким именем уже существует существует!");
            return "reg";
        }
        user.setRole(Role.USER);
        repoUsers.save(user);

        return "redirect:/login";
    }
}
