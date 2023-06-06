package com.lib.controllers;

import com.lib.models.Books;
import com.lib.models.enums.Genre;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CatalogCont extends Main {

    @GetMapping("/catalog/all")
    public String catalog_main(Model model) {
        List<Books> books = repoBooks.findAll();
        model.addAttribute("books", books);
        model.addAttribute("role", checkUserRole());
        return "catalog";
    }

    @PostMapping("/catalog/book_search")
    public String catalog_search(@RequestParam Genre genre,
/*                                 @RequestParam String date_range, */
                                 Model model) {
        List<Books> books = repoBooks.findAllByGenre(genre);

  /*      String[] date = date_range.split(" ");
        int with = Integer.parseInt(date[0]), before = Integer.parseInt(date[2]);*/

        List<Books> res = new ArrayList<>();
        for (Books g : books) {
            System.out.println("1 " + g.getYear());
            //        if (with <= g.getYear() && g.getYear() <= before) {
                res.add(g);
                System.out.println("2 " + g.getYear());
          //  }
        }

        model.addAttribute("books", res);
        model.addAttribute("role", checkUserRole());
        return "catalog";
    }

    @GetMapping("/catalog/genre/{genre}")
    public String catalog_genre_search(@PathVariable(value = "genre") Genre genre, Model model) {
        List<Books> res = repoBooks.findAllByGenre(genre);

        model.addAttribute("books", res);
        model.addAttribute("role", checkUserRole());
        return "catalog";
    }

    @GetMapping("/catalog/year/{year}")
    public String catalog_year_search(@PathVariable(value = "year") int year, Model model) {
        List<Books> books = repoBooks.findAllByYear(year);
        model.addAttribute("books", books);
        model.addAttribute("role", checkUserRole());
        return "catalog";
    }

    @PostMapping("/catalog/search")
    public String catalogSearch(@RequestParam String search, Model model) {
        List<Books> temp = repoBooks.findAll(), books = new ArrayList<>();
        for (Books g : temp) if (g.getName().contains(search)) books.add(g);
        model.addAttribute("books", books);
        model.addAttribute("role", checkUserRole());
        return "catalog";
    }
}
