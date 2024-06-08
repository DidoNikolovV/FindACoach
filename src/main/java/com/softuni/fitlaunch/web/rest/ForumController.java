package com.softuni.fitlaunch.web.rest;


import com.softuni.fitlaunch.model.entity.TopicEntity;
import com.softuni.fitlaunch.service.TopicService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/forum")
public class ForumController {

    private final TopicService topicService;

    public ForumController(TopicService topicService) {
        this.topicService = topicService;
    }

    @GetMapping
    public String showForum(Model model, @PageableDefault(
            size = 3,
            sort = "id"
    ) Pageable pageable) {
        Page<TopicEntity> topics = topicService.getAllTopics(pageable);
        model.addAttribute("topics", topics);

        return "forum";
    }
}
