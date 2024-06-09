package com.softuni.fitlaunch.web.rest;


import com.softuni.fitlaunch.model.dto.TopicCommentDTO;
import com.softuni.fitlaunch.model.dto.TopicDTO;
import com.softuni.fitlaunch.model.entity.TopicEntity;
import com.softuni.fitlaunch.service.CommentService;
import com.softuni.fitlaunch.service.TopicService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/forum")
public class ForumController {

    private final TopicService topicService;
    private final CommentService commentService;

    public ForumController(TopicService topicService, CommentService commentService) {
        this.topicService = topicService;
        this.commentService = commentService;
    }

    @GetMapping
    public String showForum(Model model, @PageableDefault(
            size = 3,
            sort = "id"
    ) Pageable pageable) {

        Page<TopicEntity> topics = topicService.getAllTopics(pageable);
        model.addAttribute("topics", topics);
        model.addAttribute("activePage", "forum");

        return "forum";
    }

    @GetMapping("/topic/{id}")
    public String viewTopicDiscussion(@PathVariable("id") Long id,
                                      @RequestParam(defaultValue = "0") int page,
                                      Model model) {
        TopicDTO topic = topicService.getById(id);
        Page<TopicCommentDTO> commentsPage = commentService.findByTopicId(id, PageRequest.of(page, 10));

        model.addAttribute("topic", topic);
        model.addAttribute("comments", commentsPage.getContent());
        model.addAttribute("commentsPage", commentsPage);

        return "discussion";
    }
}
