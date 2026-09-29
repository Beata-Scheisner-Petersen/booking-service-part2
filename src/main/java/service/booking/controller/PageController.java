package service.booking.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/reservation")
    public String reservation(Model model) {
        model.addAttribute("message", "Here you can make reservation");
        return "reservation";
    }

    @GetMapping("/mypage")
    public String myPage() {
        return "my_page";
    }

    @GetMapping("/updateCustomer")
    public String editCustomerPage() {
        return "update_customer";
    }

    @GetMapping("/deleteAccount")
    public String deleteAccount() {
        return "delete_account";
    }

    @GetMapping("/myReservations")
    public String myReservations() {
        return "my_reservation";
    }

    @GetMapping("/myReviews")
    public String myReviews() {
        return "myReviews";
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login";
    }
}