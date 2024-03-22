package com.softuni.fitlaunch.web;

import com.softuni.fitlaunch.model.dto.user.UserDTO;
import com.softuni.fitlaunch.model.dto.user.UserRegisterDTO;
import com.softuni.fitlaunch.model.dto.view.ScheduledWorkoutView;
import com.softuni.fitlaunch.model.dto.view.UserProfileView;
import com.softuni.fitlaunch.service.BlackListService;
import com.softuni.fitlaunch.service.ScheduleWorkoutService;
import com.softuni.fitlaunch.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;


@Slf4j
@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    private final BlackListService blackListService;

    private final ScheduleWorkoutService scheduleWorkoutService;


    public UserController(UserService userService, BlackListService blackListService, ScheduleWorkoutService scheduleWorkoutService) {
        this.userService = userService;
        this.blackListService = blackListService;
        this.scheduleWorkoutService = scheduleWorkoutService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/profile")
    public String userProfile(Principal principal, Model model) {
        UserProfileView userProfileView = userService.getUserProfileByUsername(principal.getName());


        List<ScheduledWorkoutView> upcomingSessions = scheduleWorkoutService.getAllScheduledWorkouts(userProfileView.getUsername());

        model.addAttribute("user", userProfileView);
        model.addAttribute("upcomingSessions", upcomingSessions);

        return "profile";
    }

    @PostMapping("/profile")
    public String userProfile(Principal principal, Model model, UserProfileView userProfileView) {

        UserProfileView profileView = userService.uploadProfilePicture(principal.getName(), userProfileView.getImgUrl());

        model.addAttribute("user", profileView);

        return "redirect:/users/profile";
    }

    @PostMapping("/login-error")
    public String onFailure(@ModelAttribute("username") String username,
                            Model model) {
        UserDTO user = userService.getUserByUsername(username);

        if (user.isActivated()) {
            model.addAttribute("username", username);
            model.addAttribute("bad_credentials", "true");
        } else {
            model.addAttribute("user_not_active", "true");
        }

        return "login";
    }


    @GetMapping("/register")
    public ModelAndView register(@ModelAttribute("userRegisterDTO") UserRegisterDTO userRegisterDTO) {
        return new ModelAndView("register");
    }

    @PostMapping("/register")
    public ModelAndView register(@ModelAttribute("userRegisterDTO") @Valid UserRegisterDTO userRegisterDTO,
                                 BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return new ModelAndView("register");
        }


        boolean hasSuccessfullyRegistered = userService.register(userRegisterDTO);
        if (!hasSuccessfullyRegistered) {
            ModelAndView modelAndView = new ModelAndView("register");
            modelAndView.addObject("hasRegisterError", true);
            return modelAndView;
        }

        return new ModelAndView("redirect:/users/login");
    }

    @GetMapping("/all")
    public String allUsers(HttpServletRequest request, Model model) {
        String ipAddress = request.getRemoteAddr();
        request.getSession().setAttribute("userIpAddress", ipAddress);

        List<UserDTO> allUsers = userService.getAllUsers();

        model.addAttribute("users", allUsers);

        return "users";
    }

    @PostMapping("/all")
    public String allUsers(@RequestParam("username") String username, @RequestParam("role") String role) {
        userService.changeUserRole(username, role);

        return "redirect:/users/all";
    }

    @GetMapping("/{username}/calendar")
    public String myCalendar(@PathVariable("username") String username, Model model) {
        UserDTO userByUsername = userService.getUserByUsername(username);
        model.addAttribute("userTitle", userByUsername.getTitle().name());

        return "my-calendar";
    }

    @PostMapping("/ban")
    @Secured("ROLE_ADMIN")
    public String banUser(@RequestParam("ipAddress") String ipAddress) {
        blackListService.banUser(ipAddress);

        return "redirect:/users/all";
    }

    @GetMapping("/contact-us")
    public String contactUs() {
        return "contact-us";
    }

    @GetMapping("/upgrade")
    public String membershipPlans() {
        return "upgrade";
    }

    @PostMapping("/upgrade/{membership}")
    public String membershipPlans(@PathVariable("membership") String membership, Principal principal) {
        UserDTO loggedUser = userService.getUserByUsername(principal.getName());

        userService.changeMembership(loggedUser, membership);

        return "upgrade";
    }

    @GetMapping("/activate/{activationCode}")
    public String activateAccount(@PathVariable("activationCode") String activationCode, Model model) {
        log.info("Received activation code: " + activationCode);

        if (activationCode == null || activationCode.isEmpty()) {
            model.addAttribute("activationError", "Invalid activation code");
            return "email/activation-failed";
        }

        boolean activationSuccess = userService.activateUser(activationCode);

        return activationSuccess ? "email/activation-success" : "email/activation-failed";
    }
}
