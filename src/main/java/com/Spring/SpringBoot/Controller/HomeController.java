package com.Spring.SpringBoot.Controller;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.Spring.SpringBoot.Models.Post;
import com.Spring.SpringBoot.Services.PostService;


@Controller
public class HomeController {
    @Autowired
    private PostService postService;
    @GetMapping("/")
    public String home(Model model, @RequestParam(required = false, name = "sort_by",defaultValue = "createdAt") String sort_by,
    @RequestParam(required = false, name = "per_page",defaultValue = "2") String per_page,
    @RequestParam(required = false, name = "page",defaultValue = "1") String page){
        
        Page<Post> posts_on_page = postService.getAll(Integer.parseInt(page)-1, Integer.parseInt(per_page), sort_by);
        int totalPages = posts_on_page.getTotalPages();
        List<Integer> pages = new ArrayList<>();
        if (totalPages > 0) {
            pages = IntStream.rangeClosed(0, totalPages-1)
            .boxed().collect(Collectors.toList());
        }
        List<String> links = new ArrayList<>();
        if (pages != null) {
            for (int link : pages) {
                String active = "";
                if (link == posts_on_page.getNumber()) {
                    active = "active";
                }
                String tempLink = "/?per_page="+per_page+"&page="+(link+1)+"&sort_by="+sort_by;
                links.add("<li class=\"page-item "+active+"\"><a href=\""+tempLink+"\" class='page-link'>"+(link+1)+"</a></li>");
            }
            model.addAttribute("links", links);
        }
        
        // List<Post> posts = postService.getAll();
        model.addAttribute("posts",posts_on_page);
        return "home";
    }
}
