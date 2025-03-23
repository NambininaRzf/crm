package site.easy.to.build.crm.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.CustomerBudget;
import site.easy.to.build.crm.entity.CustomerExpenses;
import site.easy.to.build.crm.entity.OAuthUser;
import site.easy.to.build.crm.entity.Ticket;
import site.easy.to.build.crm.entity.User;
import site.easy.to.build.crm.service.customer.CustomerBudgetService;
import site.easy.to.build.crm.service.customer.CustomerExpensesService;
import site.easy.to.build.crm.service.customer.CustomerServiceImpl;
import site.easy.to.build.crm.service.lead.LeadServiceImpl;
import site.easy.to.build.crm.service.ticket.TicketServiceImpl;
import site.easy.to.build.crm.service.user.UserService;
import site.easy.to.build.crm.util.AuthenticationUtils;

@Controller
@RequestMapping("/customer-budget")
public class CustomerBudgetController {

    private final LeadServiceImpl leadService;
    private final CustomerServiceImpl customerServiceImpl;
    private final CustomerBudgetService customerBudgetService;
    private final AuthenticationUtils authenticationUtils;
    private final UserService userService;

    @Autowired
    public CustomerBudgetController(LeadServiceImpl leadServiceImpl,CustomerServiceImpl customerServiceImpl,CustomerBudgetService customerBudgetService,
                            AuthenticationUtils authenticationUtils,UserService userService) {
        this.leadService = leadServiceImpl;
        this.customerServiceImpl = customerServiceImpl;
        this.customerBudgetService = customerBudgetService;
        this.authenticationUtils = authenticationUtils;
        this.userService = userService;
    }

    
    @GetMapping("/budget")
    public String redirectBudget(Model model,Authentication authentication) {
        int userId = authenticationUtils.getLoggedInUserId(authentication);
        User user = userService.findById(userId);
        if(user.isInactiveUser()) {
            return "error/account-inactive";
        }

        boolean gmailAccess = false;
        boolean isGoogleUser = !(authentication instanceof UsernamePasswordAuthenticationToken);
        if (isGoogleUser) {
            OAuthUser oAuthUser = authenticationUtils.getOAuthUserFromAuthentication(authentication);
            gmailAccess = authenticationUtils.checkIfAppHasAccess("https://www.googleapis.com/auth/gmail.modify", oAuthUser);
        }

        List<Customer> customers = customerServiceImpl.findAll();
        model.addAttribute("gmailAccess",gmailAccess);
        model.addAttribute("isGoogleUser",isGoogleUser);
        model.addAttribute("customers",customers);
        model.addAttribute("customerBudget", new CustomerBudget());
        return "customer-budget/customer-budget";
    }

    @PostMapping("/budget")
    public String insertTicket(@ModelAttribute("customerBudget") CustomerBudget customerBudget, BindingResult bindingResult,
                               Model model,Authentication authentication) {
        int userId = authenticationUtils.getLoggedInUserId(authentication);
        User user = userService.findById(userId);
        if(user.isInactiveUser()) {
            return "error/account-inactive";
        }
        customerBudget.setUser(user);
        customerBudget.setCreatedAt(LocalDateTime.now());
        customerBudgetService.save(customerBudget);
        return "redirect:/customer-budget/budget";
        
        
    }
}
