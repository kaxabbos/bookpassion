package com.lib.controllers;

import com.lib.models.Comments;
import com.lib.models.Books;
import com.lib.models.Users;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class BookCont extends Main {

    @GetMapping("/book/{id}")
    public String book(@PathVariable(value = "id") Long id, Model model) {
        if (!repoBooks.existsById(id)) return "redirect:/catalog";
        long userid = 0, userIdFromBD, bookid = 0, cart = 1, buy = 1;
        Users userFromDB = new Users();

        Optional<Books> temp = repoBooks.findById(id);
        List<Books> books = new ArrayList<>();
        temp.ifPresent(books::add);

        for (Books g : books) {
            userid = g.getUserid();
            bookid = g.getId();
            break;
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if ((!(auth instanceof AnonymousAuthenticationToken)) && auth != null) {
            UserDetails userDetail = (UserDetails) auth.getPrincipal();
            if (userDetail != null) userFromDB = repoUsers.findByUsername(userDetail.getUsername());
        }

        userIdFromBD = userFromDB.getId();
        if (userIdFromBD == userid) model.addAttribute("userid", userid);

        List<Comments> comments = repoComments.findAllByBookid(bookid);
        if (userFromDB.getCart() != null) {
            long[] carts = userFromDB.getCart();
            for (long c : carts)
                if (c == id) {
                    model.addAttribute("cart", cart);
                    break;
                }
        }

        if (userFromDB.getBuy() != null) {
            long[] buys = userFromDB.getBuy();
            for (long b : buys)
                if (b == id) {
                    model.addAttribute("buy", buy);
                    break;
                }
        }



        model.addAttribute("books", books);
        model.addAttribute("comments", comments);
        model.addAttribute("role", checkUserRole());
        return "book";
    }

    @PostMapping("/book/{id}/comment_add")
    public String comment_add(
            @PathVariable(value = "id") Long id,
            @RequestParam String date, @RequestParam String[] comment
    ) {
        StringBuilder com = new StringBuilder();
        for (String s : comment) com.append(s);

        String username = "";

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if ((!(auth instanceof AnonymousAuthenticationToken)) && auth != null) {
            UserDetails userDetail = (UserDetails) auth.getPrincipal();
            if (userDetail != null) {
                Users userFromDB = repoUsers.findByUsername(userDetail.getUsername());
                username = userFromDB.getUsername();
            }
        }

        Comments c = new Comments(id, username, date, com.toString());

        repoComments.save(c);
        return "redirect:/book/{id}";
    }
}
