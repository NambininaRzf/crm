package site.easy.to.build.crm.controller.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.service.customer.CustomerService;

@RestController
@RequestMapping("/api/customers")
public class CustomerRestController {
    // private final CustomerService customerService;

    // @Autowired
    // public CustomerRestController(CustomerService customerService) {
    //     this.customerService = customerService;
    // }

    // @GetMapping
    // public List<Customer> getAllCustomers() {
    //     return customerService.findAll();
    // }
}
