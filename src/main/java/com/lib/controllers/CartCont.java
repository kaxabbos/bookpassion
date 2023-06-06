package com.lib.controllers;

import com.lib.models.BookIncome;
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

import java.util.ArrayList;
import java.util.List;

@Controller
public class CartCont extends Main {

    @GetMapping("/cart")
    public String cart(Model model) {
        Users userFromDB = new Users();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if ((!(auth instanceof AnonymousAuthenticationToken)) && auth != null) {
            UserDetails userDetail = (UserDetails) auth.getPrincipal();
            if (userDetail != null) {
                userFromDB = repoUsers.findByUsername(userDetail.getUsername());
            }
        }

        if (userFromDB.getCart() != null) {
            long[] cart = userFromDB.getCart();
            float summary = 0;
            List<Books> books = new ArrayList<>(), temp = repoBooks.findAll();

            for (Books g : temp) for (long c : cart) if (g.getId() == c) books.add(g);
            for (Books g : books) summary += g.getPrice();

            model.addAttribute("summary", summary);
            model.addAttribute("books", books);
            int i = 0;
            for (Books g : books) {
                i++;
                if (i == 2) {
                    model.addAttribute("more", i);
                    break;
                }
            }
        }

        model.addAttribute("role", checkUserRole());
        return "cart";
    }

    @PostMapping("/book/{id}/add_cart")
    public String add_cart(@PathVariable(value = "id") Long id) {
        Users userFromDB = new Users();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if ((!(auth instanceof AnonymousAuthenticationToken)) && auth != null) {
            UserDetails userDetail = (UserDetails) auth.getPrincipal();
            if (userDetail != null) userFromDB = repoUsers.findByUsername(userDetail.getUsername());
        }

        long[] cart;
        if (userFromDB.getCart() == null) cart = new long[]{id};
        else {
            cart = new long[userFromDB.getCart().length + 1];
            for (int i = 0; i < userFromDB.getCart().length; i++) cart[i] = userFromDB.getCart()[i];
            cart[userFromDB.getCart().length] = id;
        }

        userFromDB.setCart(cart);

        repoUsers.save(userFromDB);
        return "redirect:/book/{id}";
    }

    @PostMapping("/book/{id}/remove_cart")
    public String remove_cart(@PathVariable(value = "id") Long id) {
        Users userFromDB = new Users();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if ((!(auth instanceof AnonymousAuthenticationToken)) && auth != null) {
            UserDetails userDetail = (UserDetails) auth.getPrincipal();
            if (userDetail != null) userFromDB = repoUsers.findByUsername(userDetail.getUsername());
        }

        if (userFromDB.getCart().length == 1) userFromDB.setCart(null);
        else {
            long[] cart = new long[userFromDB.getCart().length - 1];
            int i = 0;
            for (long c : userFromDB.getCart()) {
                if (id == c) continue;
                cart[i] = c;
                i++;
            }
            userFromDB.setCart(cart);
        }

        repoUsers.save(userFromDB);
        return "redirect:/cart";
    }

    @PostMapping("/book/remove_cart_all")
    public String remove_cart_all(Model model) {
        Users userFromDB = new Users();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if ((!(auth instanceof AnonymousAuthenticationToken)) && auth != null) {
            UserDetails userDetail = (UserDetails) auth.getPrincipal();
            if (userDetail != null) userFromDB = repoUsers.findByUsername(userDetail.getUsername());
        }

        userFromDB.setCart(null);

        repoUsers.save(userFromDB);
        return "redirect:/cart";
    }

    @PostMapping("/book/{id}/buy")
    public String buy(@PathVariable(value = "id") Long id) {
        Users userFromDB = new Users();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if ((!(auth instanceof AnonymousAuthenticationToken)) && auth != null) {
            UserDetails userDetail = (UserDetails) auth.getPrincipal();
            if (userDetail != null) userFromDB = repoUsers.findByUsername(userDetail.getUsername());
        }

        if (userFromDB.getCart() != null) {
            if (userFromDB.getCart().length == 1) userFromDB.setCart(null);
            else {
                long[] cart = new long[userFromDB.getCart().length - 1];
                int i = 0;
                for (long c : userFromDB.getCart()) {
                    if (id == c) continue;
                    cart[i] = c;
                    i++;
                }
                userFromDB.setCart(cart);
            }
        }

        long[] buy;
        if (userFromDB.getBuy() == null) buy = new long[]{id};
        else {
            buy = new long[userFromDB.getBuy().length + 1];
            for (int i = 0; i < userFromDB.getBuy().length; i++) buy[i] = userFromDB.getBuy()[i];
            buy[userFromDB.getBuy().length] = id;
        }

        BookIncome bookIncome = repoBookIncome.findById(id).orElseThrow();

        bookIncome.setCount(bookIncome.getCount() + 1);
        bookIncome.setIncome(bookIncome.getIncome() + bookIncome.getPrice());
        repoBookIncome.save(bookIncome);

        userFromDB.setBuy(buy);

        repoUsers.save(userFromDB);
        return "redirect:/book/{id}";
    }

    @PostMapping("/book/buy_cart_all")
    public String buy_cart_all(Model model) {
        Users userFromDB = new Users();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if ((!(auth instanceof AnonymousAuthenticationToken)) && auth != null) {
            UserDetails userDetail = (UserDetails) auth.getPrincipal();
            if (userDetail != null) userFromDB = repoUsers.findByUsername(userDetail.getUsername());
        }

        long[] buy;
        if (userFromDB.getBuy() == null) {
            buy = new long[userFromDB.getCart().length];
            for (int i = 0; i < userFromDB.getCart().length; i++) buy[i] = userFromDB.getCart()[i];
        } else {
            buy = new long[userFromDB.getBuy().length + userFromDB.getCart().length];
            for (int i = 0; i < buy.length; i++) {
                for (int j = 0; j < userFromDB.getBuy().length; j++) {
                    buy[i] = userFromDB.getBuy()[j];
                    i++;
                }
                for (int j = 0; j < userFromDB.getCart().length; j++) {
                    buy[i] = userFromDB.getCart()[j];
                    BookIncome g = repoBookIncome.findById(userFromDB.getCart()[j]).orElseThrow();
                    g.setCount(g.getCount() + 1);
                    g.setIncome(g.getIncome() + g.getPrice());
                    repoBookIncome.save(g);
                    i++;
                }
            }
        }

        userFromDB.setBuy(buy);
        userFromDB.setCart(null);

        repoUsers.save(userFromDB);

        return "redirect:/cart";
    }
}
