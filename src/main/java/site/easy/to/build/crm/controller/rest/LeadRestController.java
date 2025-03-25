package site.easy.to.build.crm.controller.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import site.easy.to.build.crm.entity.Lead;
import site.easy.to.build.crm.entity.Ticket;
import site.easy.to.build.crm.model.dto.ConfigRequest;
import site.easy.to.build.crm.security.JwtUtil;
import site.easy.to.build.crm.service.lead.LeadService;
import site.easy.to.build.crm.service.ticket.TicketService;

@RestController
@RequestMapping ("/api/leads")
public class LeadRestController {
    private final LeadService leadService;
    private final JwtUtil jwtUtil;

    @Autowired
    public LeadRestController(LeadService leadService,JwtUtil jwtUtil){
        this.leadService = leadService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public ResponseEntity<List<Lead>> getAllLeads(@RequestBody ConfigRequest tokenRequest){
        String token = tokenRequest.getToken();
        String username = tokenRequest.getUsername();

        if (jwtUtil.validateToken(token, username)) {
            List<Lead> tickets = leadService.findAll();
            return ResponseEntity.ok(tickets);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }
}
