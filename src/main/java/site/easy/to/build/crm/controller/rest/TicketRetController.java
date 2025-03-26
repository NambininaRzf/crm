package site.easy.to.build.crm.controller.rest;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import site.easy.to.build.crm.entity.CustomerExpenses;
import site.easy.to.build.crm.entity.TauxStorage;
import site.easy.to.build.crm.entity.Ticket;
import site.easy.to.build.crm.model.dto.ConfigRequest;
import site.easy.to.build.crm.security.JwtUtil;
import site.easy.to.build.crm.service.customer.CustomerExpensesService;
import site.easy.to.build.crm.service.ticket.TicketService;

@RestController
@RequestMapping ("/api/tickets")
public class TicketRetController {
    private final TicketService ticketService;
    private final JwtUtil jwtUtil;
    private final CustomerExpensesService customerExpensesService;

    @Autowired
    public TicketRetController(TicketService ticketService,JwtUtil jwtUtil,CustomerExpensesService customerExpensesService){
        this.ticketService = ticketService;
        this.jwtUtil = jwtUtil;
        this.customerExpensesService = customerExpensesService;
    }

    @PostMapping
    public ResponseEntity<List<Ticket>> getAllTickets(@RequestBody ConfigRequest tokenRequest){
        String token = tokenRequest.getToken();
        String username = tokenRequest.getUsername();

        if (jwtUtil.validateToken(token, username)) {
            List<Ticket> tickets = ticketService.findAll();
            return ResponseEntity.ok(tickets);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    @PostMapping("/delete")
    public ResponseEntity<String> updateTaux(@RequestBody ConfigRequest tokenRequest) {
        System.out.println("ETO");
        String token = tokenRequest.getToken();
        String username = tokenRequest.getUsername();
        int idTicketDelete = tokenRequest.getIdTicketDelete();
        List<CustomerExpenses> expensesTicket = tokenRequest.getCustomerExpensesTicketDelete();

        System.out.println("Param ticketId: " + idTicketDelete);

        if (jwtUtil.validateToken(token, username)) {
            try {
                for (CustomerExpenses customerExpenses : expensesTicket) {
                    customerExpensesService.deleteById(customerExpenses.getId());
                }
                Ticket toDelete = ticketService.findByTicketId(idTicketDelete);
                ticketService.delete(toDelete);
                return ResponseEntity.ok("succes");
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                     .body("Erreur lors de la suppression du ticket :" + e.getMessage());
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("ACCESS NOT ALLOWED");
    }
}
