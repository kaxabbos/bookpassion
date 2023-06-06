package com.lib.controllers;

import com.lib.models.Author;
import com.lib.models.Books;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class AuthorAddEditCont extends Main{

    @GetMapping("author/add")
    public String author_add(Model model){
        model.addAttribute("role", checkUserRole());
        return "author_add";
    }

    @PostMapping("author/add")
    public String author_add(
            @RequestParam String name, @RequestParam("poster") MultipartFile poster,
            @RequestParam int birthday, @RequestParam int die, @RequestParam String[] description
    ) throws IOException {
        Author newAuthor = new Author(name, birthday, die);

        StringBuilder des = new StringBuilder();
        for (String s : description) des.append(s);
        newAuthor.setDescription(des.toString());

        String uuidFile = UUID.randomUUID().toString();

        if (poster != null && !poster.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) uploadDir.mkdir();
            String result_poster = uuidFile + "_" + poster.getOriginalFilename();
            poster.transferTo(new File(uploadPath + "/" + result_poster));
            newAuthor.setPoster(result_poster);
        }

        repoAuthor.save(newAuthor);
        return "redirect:/authors";
    }

    @GetMapping("/author/{id}/edit")
    public String author_edit(@PathVariable(value = "id") Long id, Model model) {
        if (!repoAuthor.existsById(id)) return "redirect:/authors";
        Optional<Author> temp = repoAuthor.findById(id);
        ArrayList<Author> authors = new ArrayList<>();
        temp.ifPresent(authors::add);
        model.addAttribute("authors", authors);
        model.addAttribute("role", checkUserRole());
        return "author_edit";
    }

    @PostMapping("author/{id}/edit")
    public String author_edit( @PathVariable(value = "id") Long id,
            @RequestParam String name, @RequestParam("poster") MultipartFile poster,
            @RequestParam int birthday, @RequestParam int die, @RequestParam String[] description
    ) throws IOException {
        Author author = repoAuthor.findById(id).orElseThrow();

        author.setName(name);
        author.setBirthday(birthday);
        author.setDie(die);

        StringBuilder des = new StringBuilder();
        for (String s : description) des.append(s);
        author.setDescription(des.toString());

        String uuidFile = UUID.randomUUID().toString();

        if (poster != null && !poster.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) uploadDir.mkdir();
            String result_poster = uuidFile + "_" + poster.getOriginalFilename();
            poster.transferTo(new File(uploadPath + "/" + result_poster));
            author.setPoster(result_poster);
        }

        repoAuthor.save(author);
        return "redirect:/authors";
    }

    @GetMapping("/author/{id}/delete")
    public String author_delete(@PathVariable(value = "id") Long id) {
        repoAuthor.deleteById(id);
        List<Books> booksList = repoBooks.findAllByAuthorid(id);
        for (Books b: booksList) {
            repoBooks.deleteById(b.getId());
            repoBookIncome.deleteById(b.getId());
        }
        return "redirect:/authors";
    }
}
