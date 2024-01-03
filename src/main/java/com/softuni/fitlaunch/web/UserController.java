package com.softuni.fitlaunch.web;

import com.softuni.fitlaunch.model.dto.program.ProgramWeekWorkoutDTO;
import com.softuni.fitlaunch.model.dto.user.CoachDTO;
import com.softuni.fitlaunch.model.dto.user.UserDTO;
import com.softuni.fitlaunch.model.dto.user.UserProfileDTO;
import com.softuni.fitlaunch.model.dto.user.UserRegisterDTO;
import com.softuni.fitlaunch.model.dto.view.UserProfileView;
import com.softuni.fitlaunch.model.dto.workout.ScheduledWorkoutDTO;
import com.softuni.fitlaunch.model.entity.UserRoleEntity;
import com.softuni.fitlaunch.model.enums.UserRoleEnum;
import com.softuni.fitlaunch.service.BlackListService;
import com.softuni.fitlaunch.service.CoachService;
import com.softuni.fitlaunch.service.ScheduleWorkoutService;
import com.softuni.fitlaunch.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final CoachService coachService;

    private final BlackListService blackListService;

    private final ScheduleWorkoutService scheduleWorkoutService;



    public UserController(UserService userService, CoachService coachService, BlackListService blackListService, ScheduleWorkoutService scheduleWorkoutService) {
        this.userService = userService;
        this.coachService = coachService;
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

        model.addAttribute("user", userProfileView);

        return "profile";
    }

    @PostMapping("/profile")
    public String userProfile(Principal principal, Model model, UserProfileDTO userProfileDTO) throws IOException {

        UserProfileView userProfileView = userService.uploadProfilePicture(principal.getName(), userProfileDTO.getImgUrl());

        model.addAttribute("user", userProfileView);

        return "redirect:/users/profile";
    }

    @PostMapping("/login-error")
    public String onFailure(@ModelAttribute("username") String username,
                            Model model) {

        UserDTO user = userService.getUserByUsername(username);

        if(user != null) {
            if(user.isActivated()) {
                model.addAttribute("username", username);
                model.addAttribute("bad_credentials", "true");
            } else {
                model.addAttribute("user_not_active", "true");

            }
        } else {
            model.addAttribute("username", username);
            model.addAttribute("bad_credentials", "true");
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

        if(bindingResult.hasErrors()) {
            return new ModelAndView("register");
        }


        boolean hasSuccessfullyRegistered = userService.register(userRegisterDTO);
        if(!hasSuccessfullyRegistered) {
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
        Long roleId = role.equals("ADMIN") ? 1L : 2L;

        UserRoleEntity newRole = new UserRoleEntity()
                .setId(roleId)
                .setRole(UserRoleEnum.valueOf(role));
        userService.changeUserRole(username, newRole);

        return "redirect:/users/all";
    }

    @GetMapping("/{username}/calendar")
    public String myCalendar(@PathVariable("username") String username, Model model) {
//        CoachDTO coachByUsername = coachService.getCoachByUsername(username);
//        List<ScheduledWorkoutDTO> allCoachScheduledWorkouts = scheduleWorkoutService.getAllCoachScheduledWorkouts(coachByUsername);
//
//
//        UserDTO userByUsername = userService.getUserByUsername(username);
//
//        model.addAttribute("scheduledWorkouts", allCoachScheduledWorkouts);
//        model.addAttribute("user", userByUsername);
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

        System.out.println("Received activation code: " + activationCode);

        if(activationCode == null || activationCode.isEmpty()) {
            model.addAttribute("activationError", "Invalid activation code");
            return "email/activation-failed";
        }

        boolean activationSuccess = userService.activateUser(activationCode);
        if(activationSuccess) {
            return "email/activation-success";
        } else {
            return "email/activation-failed";
        }

    }

    @GetMapping("/progress")
    public String progress(Principal principal, Model model) {
        UserDTO loggedUser = userService.getUserByUsername(principal.getName());

        List<ProgramWeekWorkoutDTO> workoutsCompleted = loggedUser.getWorkoutsCompleted();

        model.addAttribute("workoutsCompleted", workoutsCompleted);
        return "workout-history";
    }

}
