package gbrstd.clinica.controller;

import gbrstd.clinica.dto.SignUpDto;
import gbrstd.clinica.exceptions.UsernameAlreadyExistException;
import gbrstd.clinica.security.AppUserDetailsService;
import gbrstd.clinica.service.UserService;
import gbrstd.clinica.exceptions.PasswordMatchException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class AuthController {

    private final UserService userService;

    private final AppUserDetailsService userDetailsService;

    public AuthController(UserService userService, AppUserDetailsService userDetailsService) {
        this.userService = userService;
        this.userDetailsService = userDetailsService;
    }

    @GetMapping("/login")
    public String login() {
        if (isAuthenticated()) {
            return "redirect:/";
        }

        return "auth/login";
    }

    @GetMapping("/signup")
    public String showSignUpForm(Model model) {
        if (isAuthenticated()) {
            return "redirect:/";
        }

        model.addAttribute("signUpForm", new SignUpDto());

        return "auth/signup";
    }

    @PostMapping("/signup")
    public String signUpSubmit(@ModelAttribute("signUpForm") @Valid SignUpDto signUpDto,
                               BindingResult result,
                               Model model) {

        boolean hasErrors = result.hasErrors();

        if (!hasErrors) {
            try {
                userService.signUp(signUpDto);
            } catch (UsernameAlreadyExistException e) {
                result.rejectValue("username", "error.username", e.getMessage());
                hasErrors = true;
            } catch (PasswordMatchException e) {
                result.rejectValue("password", "error.password", e.getMessage());
                hasErrors = true;
            }
        }

        if (hasErrors) {
            model.addAttribute("signUpForm", signUpDto);
            return "auth/signup";
        }

        // Auto login
        autoLogin(signUpDto);

        return "redirect:/";
    }

    private boolean isAuthenticated() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken);
    }

    private void autoLogin(SignUpDto signUpDto) {
        UserDetails principal = userDetailsService.loadUserByUsername(signUpDto.getUsername());

        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, signUpDto.getPassword(), principal.getAuthorities());

        final SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

}
