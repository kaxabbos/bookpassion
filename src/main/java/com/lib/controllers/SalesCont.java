package com.lib.controllers;

import com.lib.models.BookIncome;
import com.lib.models.Books;
import com.lib.models.Users;
import com.lib.models.enums.Genre;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
public class SalesCont extends Main {

    @GetMapping("/sales")
    public String sales(Model model) {
        Users userFromDB = new Users();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if ((!(auth instanceof AnonymousAuthenticationToken)) && auth != null) {
            UserDetails userDetail = (UserDetails) auth.getPrincipal();
            if (userDetail != null) userFromDB = repoUsers.findByUsername(userDetail.getUsername());
        }

        List<BookIncome> bookIncomes = repoBookIncome.findAllByUserid(userFromDB.getId());
        int[] genre = new int[Genre.values().length];
        List<Genre> genreList = Arrays.stream(Genre.values()).toList();
        float income = 0;
        for (BookIncome g : bookIncomes) {
            income += g.getIncome();
            for (Genre i : genreList) {
                int index = 0;
                Books book = repoBooks.getOne(g.getBookid());
                if (i == book.getGenre()) genre[index] += g.getIncome();
            }
        }

        model.addAttribute("income", income);
        model.addAttribute("genre", genre);
        model.addAttribute("books", bookIncomes);
        model.addAttribute("role", checkUserRole());

        bookIncomes.sort(Comparator.comparing(BookIncome::getIncome));
        Collections.reverse(bookIncomes);

        String[] topName = new String[5];
        float[] topNum = new float[5];

        for (int i = 0; i < bookIncomes.size(); i++) {
            if (i == 5) break;
            topName[i] = bookIncomes.get(i).getBookname();
            topNum[i] = bookIncomes.get(i).getIncome();
        }
        model.addAttribute("topName", topName);
        model.addAttribute("topNum", topNum);

        return "sales";
    }
}
