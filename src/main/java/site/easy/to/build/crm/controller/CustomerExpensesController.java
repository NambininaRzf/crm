package site.easy.to.build.crm.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.CustomerExpenses;
import site.easy.to.build.crm.entity.Lead;
import site.easy.to.build.crm.entity.OAuthUser;
import site.easy.to.build.crm.entity.Ticket;
import site.easy.to.build.crm.entity.User;
import site.easy.to.build.crm.google.service.gmail.GoogleGmailApiService;
import site.easy.to.build.crm.service.customer.CustomerExpensesService;
import site.easy.to.build.crm.service.lead.LeadServiceImpl;
import site.easy.to.build.crm.service.role.RoleService;
import site.easy.to.build.crm.service.ticket.TicketServiceImpl;
import site.easy.to.build.crm.service.user.UserProfileService;
import site.easy.to.build.crm.service.user.UserService;
import site.easy.to.build.crm.util.AuthenticationUtils;
import site.easy.to.build.crm.util.myexception.DepassementException;
import site.easy.to.build.crm.util.myexception.TauxAtteintException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Controller
@RequestMapping("/customer-expenses")
public class CustomerExpensesController {
    private final LeadServiceImpl leadService;
    private final TicketServiceImpl ticketService;
    private final CustomerExpensesService customerExpensesService;
    private final AuthenticationUtils authenticationUtils;
    private final UserService userService;

    @Autowired
    public CustomerExpensesController(LeadServiceImpl leadServiceImpl,TicketServiceImpl ticketServiceImpl,CustomerExpensesService customerExpensesService,
                            AuthenticationUtils authenticationUtils,UserService userService) {
        this.leadService = leadServiceImpl;
        this.ticketService = ticketServiceImpl;
        this.customerExpensesService = customerExpensesService;
        this.authenticationUtils = authenticationUtils;
        this.userService = userService;
    }
    
    @GetMapping("/ticket")
    public String redirectTicket(Model model,Authentication authentication) {
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

        List<Ticket> tickets = ticketService.findAll();
        model.addAttribute("gmailAccess",gmailAccess);
        model.addAttribute("isGoogleUser",isGoogleUser);
        model.addAttribute("tickets",tickets);
        model.addAttribute("customerExpenses", new CustomerExpenses());
        return "customer-expenses/ticket-expenses";
    }

    @GetMapping("/lead")  
    public String redirectLead(Model model,Authentication authentication) {
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

        List<Lead> leads = leadService.findAll();
        model.addAttribute("gmailAccess",gmailAccess);
        model.addAttribute("isGoogleUser",isGoogleUser);
        model.addAttribute("leads",leads);
        model.addAttribute("customerExpenses", new CustomerExpenses());
        return "customer-expenses/lead-expenses";
    }
    
    @PostMapping("/ticket")
    public String insertTicket(@ModelAttribute("customerExpenses") CustomerExpenses customerExpenses, BindingResult bindingResult,
                               Model model,Authentication authentication, RedirectAttributes redirectAttributes) {
        int userId = authenticationUtils.getLoggedInUserId(authentication);
        User user = userService.findById(userId);
        if(user.isInactiveUser()) {
            return "error/account-inactive";
        }
        customerExpenses.setUser(user);
        customerExpenses.setCreatedAt(LocalDateTime.now());
        Customer customer = customerExpenses.getTicket().getCustomer();
        customerExpenses.setCustomer(customer);
        System.out.println("CUSTOMER ID:" +customerExpenses.getId());
        CustomerExpenses saved = customerExpensesService.save(customerExpenses);
        try {
            customerExpensesService.insertExpenses(saved);
        } catch (TauxAtteintException e) {
            System.out.println("FAILED:" + e.getMessage());
            // e.printStackTrace();

                // Ajouter un message d'alerte pour l'exception
            redirectAttributes.addFlashAttribute("warningMessage", e.getMessage());
            
            // Rediriger vers la page de ticket avec l'alerte
            return "redirect:/customer-expenses/ticket";
        }catch(DepassementException ex){
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            redirectAttributes.addFlashAttribute("expenses", ex.getCustomerExpenses());
            // Rediriger vers la page de ticket avec l'alerte
            return "redirect:/customer-expenses/ticket";
        }catch(Exception exp){
            exp.printStackTrace();
        }
        
        return "redirect:/customer-expenses/ticket";
 
    }

    @PostMapping("/lead")
    public String insertLead(@ModelAttribute("customerExpenses") CustomerExpenses customerExpenses, BindingResult bindingResult,
                               Model model,Authentication authentication,RedirectAttributes redirectAttributes) {
        int userId = authenticationUtils.getLoggedInUserId(authentication);
        User user = userService.findById(userId);
        if(user.isInactiveUser()) {
            return "error/account-inactive";
        }
        customerExpenses.setUser(user);
        customerExpenses.setCreatedAt(LocalDateTime.now());
        Customer customer = customerExpenses.getLead().getCustomer();
        customerExpenses.setCustomer(customer);
        System.out.println("CUSTOMER ID:" +customerExpenses.getId());
        CustomerExpenses saved = customerExpensesService.save(customerExpenses);
        try {
            customerExpensesService.insertExpenses(saved);
        } catch (TauxAtteintException e) {
            System.out.println("FAILED:" + e.getMessage());
            // e.printStackTrace();

                // Ajouter un message d'alerte pour l'exception
            redirectAttributes.addFlashAttribute("warningMessage", e.getMessage());
            
            // Rediriger vers la page de ticket avec l'alerte
            return "redirect:/customer-expenses/lead";
        }catch(DepassementException ex){
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            redirectAttributes.addFlashAttribute("expenses", ex.getCustomerExpenses());
            // Rediriger vers la page de ticket avec l'alerte
            return "redirect:/customer-expenses/lead";
        }catch(Exception exp){
            exp.printStackTrace();
        }
        
        return "redirect:/customer-expenses/lead";        
    }

    @PostMapping("/confirm-expense-ticket")
    public String confirmExpensesTicket(@ModelAttribute("expenses") CustomerExpenses customerExpenses, BindingResult bindingResult,
                               Model model,Authentication authentication) {
        try {
            System.out.println("Here whe are");
            CustomerExpenses real = customerExpensesService.findById(customerExpenses.getId());
            customerExpensesService.confirm(real);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/customer-expenses/ticket";
    }

    // @PostMapping("/confirm-expense-lead")
    // public String confirmExpensesLead(@ModelAttribute("expenses") CustomerExpenses customerExpenses, BindingResult bindingResult,
    //                            Model model,Authentication authentication) {
    //     try {
    //         System.out.println("Here whe are");
    //         CustomerExpenses real = customerExpensesService.findById(customerExpenses.getId());
    //         customerExpensesService.confirm(real);
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    //     return "redirect:/customer-expenses/lead";
    // }
    
}
