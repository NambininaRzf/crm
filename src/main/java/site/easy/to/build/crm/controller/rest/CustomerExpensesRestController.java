package site.easy.to.build.crm.controller.rest;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.CustomerExpenses;
import site.easy.to.build.crm.model.dto.ConfigRequest;
import site.easy.to.build.crm.security.JwtUtil;
import site.easy.to.build.crm.service.customer.CustomerExpensesService;
import site.easy.to.build.crm.service.customer.CustomerService;

@RestController
@RequestMapping ("/api/expenses")
public class CustomerExpensesRestController {
    private final CustomerExpensesService customerExpensesService;
    private final JwtUtil jwtUtil;

    @Autowired
    public CustomerExpensesRestController(CustomerExpensesService customerExpensesService, JwtUtil jwtUtil) {
        this.customerExpensesService = customerExpensesService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public ResponseEntity<List<CustomerExpenses>> getAllCustomers(@RequestBody ConfigRequest tokenRequest) {
        String token = tokenRequest.getToken();
        String username = tokenRequest.getUsername();

        System.out.println("Param token: " + token);

        if (jwtUtil.validateToken(token, username)) {
            List<CustomerExpenses> expenses = customerExpensesService.findAll();
            return ResponseEntity.ok(expenses);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    // @GetMapping
    // public ResponseEntity<List<CustomerExpenses>> getAllCustomers(@RequestParam String token, @RequestParam String username) {
    //     System.out.println("PAram token:" + token);
    //     if (jwtUtil.validateToken(token, username)) {
    //         List<CustomerExpenses> expenses = customerExpensesService.findAll();
    //         return ResponseEntity.ok(expenses);
    //     }
    //     return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    // }


}
