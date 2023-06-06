package com.lib.controllers;

import com.lib.models.Author;
import com.lib.models.Books;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class AuthorCont extends Main {

    @GetMapping("authors")
    public String authors(Model model) {
        List<Author> authors = repoAuthor.findAll();
        model.addAttribute("authors", authors);
        model.addAttribute("role", checkUserRole());
        return "authors";
    }

    @GetMapping("author/{id}")
    public String author(Model model, @PathVariable(value = "id") Long id) {
        Optional<Author> temp = repoAuthor.findById(id);
        List<Author> author = new ArrayList<>();
        temp.ifPresent(author::add);

        List<Books> booksList = repoBooks.findAllByAuthorid(id);

        model.addAttribute("authors", author);
        model.addAttribute("books", booksList);
        model.addAttribute("role", checkUserRole());
        return "author";
    }


}
