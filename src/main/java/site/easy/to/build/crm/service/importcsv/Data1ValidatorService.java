package site.easy.to.build.crm.service.importcsv;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.CustomerExpenses;
import site.easy.to.build.crm.entity.CustomerLoginInfo;
import site.easy.to.build.crm.entity.Lead;
import site.easy.to.build.crm.entity.Ticket;
import site.easy.to.build.crm.entity.User;
import site.easy.to.build.crm.model.importcsv.Data1Validator;
import site.easy.to.build.crm.model.importcsv.Data2Validator;
import site.easy.to.build.crm.service.customer.CustomerExpensesService;
import site.easy.to.build.crm.service.customer.CustomerLoginInfoService;
import site.easy.to.build.crm.service.customer.CustomerService;
import site.easy.to.build.crm.service.lead.LeadService;
import site.easy.to.build.crm.service.ticket.TicketService;
import site.easy.to.build.crm.service.user.UserService;
import site.easy.to.build.crm.util.AuthenticationUtils;
import site.easy.to.build.crm.util.EmailTokenUtils;

@Service
public class Data1ValidatorService {
    public Data1Validator dataValidator;
    private final AuthenticationUtils authenticationUtils;
    private final CustomerService customerService;
    private final CustomerLoginInfoService customerLoginInfoService;
    private final UserService userService;
    private final TicketService ticketService;
    private final LeadService leadService;
    private final CustomerExpensesService customerExpensesService;

    @Autowired
    public Data1ValidatorService(AuthenticationUtils authenticationUtils,CustomerService customerService,
            CustomerLoginInfoService customerLoginInfoService,UserService userService,TicketService ticketService,
            LeadService leadService,CustomerExpensesService customerExpensesService){
        this.authenticationUtils = authenticationUtils;
        this.customerService = customerService;
        this.userService = userService;
        this.customerLoginInfoService = customerLoginInfoService;
        this.ticketService = ticketService;
        this.leadService = leadService;
        this.customerExpensesService = customerExpensesService;
    }


    public void insertData (Authentication authentication,Data1Validator data)throws Exception{
        int userId = authenticationUtils.getLoggedInUserId(authentication);
        User user = userService.findById(userId);
        try {
            // Insert Ticket
            Customer customer = customerService.findByEmail(data.getCustomerEmail());
            if (customer==null) {
                throw new Exception("customer not found");
            }
            if (data.getType().equalsIgnoreCase("ticket")) {
                Ticket ticket = new Ticket();
                ticket.setCustomer(customer);
                ticket.setSubject(data.getSubjectOrName());
                ticket.setStatus(data.getStatus());
                ticket.setManager(user);
                ticket.setEmployee(user);
                ticket.setDescription("Ticket " + data.getSubjectOrName() + " description");
                // ajouter priority
                ticket.setPriority("low");
                ticket.setCreatedAt(LocalDateTime.now());

                Ticket ticket1 = ticketService.save(ticket);
                CustomerExpenses customerExpenses = new CustomerExpenses();
                customerExpenses.setAmount(BigDecimal.valueOf(data.getExpense()));
                customerExpenses.setCreatedAt(LocalDateTime.now());
                customerExpenses.setDescription("Ticket expense description");
                customerExpenses.setTicket(ticket1);
                customerExpenses.setStatus(1);
                customerExpenses.setUser(user);
                customerExpenses.setCustomer(customer);
                // insert ticket expense
                customerExpensesService.save(customerExpenses);
            }
            if (data.getType().equalsIgnoreCase("lead")) {
                Lead lead = new Lead();
                lead.setCustomer(customer);
                lead.setName(data.getSubjectOrName());
                lead.setStatus(data.getStatus());
                lead.setGoogleDriveFolderId("0");
                lead.setCreatedAt(LocalDateTime.now());
                lead.setManager(user);
                lead.setEmployee(user);

                Lead lead1 = leadService.save(lead);
                CustomerExpenses customerExpenses = new CustomerExpenses();
                customerExpenses.setAmount(BigDecimal.valueOf(data.getExpense()));
                customerExpenses.setCreatedAt(LocalDateTime.now());
                customerExpenses.setDescription("Lead expense description");
                customerExpenses.setLead(lead1);
                customerExpenses.setStatus(1);
                customerExpenses.setUser(user);
                customerExpenses.setCustomer(customer);
                // insert ticket expense
                customerExpensesService.save(customerExpenses);

            }
        } catch (Exception e) {
            throw e;
        }
        

    }

    public void insertListData(Authentication authentication,List<Data1Validator> data) throws Exception{
        try {
            for (Data1Validator data1Validator : data) {
                insertData(authentication, data1Validator);
            }
        } catch (Exception e) {
           throw e;
        }
    }
}
