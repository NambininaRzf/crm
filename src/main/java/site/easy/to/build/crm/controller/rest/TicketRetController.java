package site.easy.to.build.crm.controller.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import site.easy.to.build.crm.entity.CustomerExpenses;
import site.easy.to.build.crm.entity.Ticket;
import site.easy.to.build.crm.model.dto.ConfigRequest;
import site.easy.to.build.crm.security.JwtUtil;
import site.easy.to.build.crm.service.ticket.TicketService;

@RestController
@RequestMapping ("/api/tickets")
public class TicketRetController {
    private final TicketService ticketService;
    private final JwtUtil jwtUtil;

    @Autowired
    public TicketRetController(TicketService ticketService,JwtUtil jwtUtil){
        this.ticketService = ticketService;
        this.jwtUtil = jwtUtil;
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
}
