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
import site.easy.to.build.crm.model.dto.ConfigRequest;
import site.easy.to.build.crm.security.JwtUtil;
import site.easy.to.build.crm.service.customer.CustomerExpensesService;
import site.easy.to.build.crm.service.taux.TauxStorageService;

@RestController
@RequestMapping ("/api/taux")   
public class TauxStorageRestController {
    private final TauxStorageService tauxStorageService;
    private final JwtUtil jwtUtil;

    @Autowired
    public TauxStorageRestController(TauxStorageService tauxStorageService, JwtUtil jwtUtil) {
        this.tauxStorageService = tauxStorageService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public ResponseEntity<TauxStorage> getTauxActuel(@RequestBody ConfigRequest tokenRequest) {
        String token = tokenRequest.getToken();
        String username = tokenRequest.getUsername();

        System.out.println("Param token: " + token);

        if (jwtUtil.validateToken(token, username)) {
            TauxStorage taux = tauxStorageService.getDefault();
            return ResponseEntity.ok(taux);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateTaux(@RequestBody ConfigRequest tokenRequest) {
        System.out.println("ETO");
        String token = tokenRequest.getToken();
        String username = tokenRequest.getUsername();
        TauxStorage tauxUpdate = tokenRequest.getTauxStorage();

        System.out.println("Param token: " + token);

        if (jwtUtil.validateToken(token, username)) {
            try {
                tauxUpdate.setCreatedAt(LocalDateTime.now());
                tauxStorageService.save(tauxUpdate);
                return ResponseEntity.ok("succes");
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                     .body("Erreur lors de l'update :" + e.getMessage());
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("ACCESS NOT ALLOWED");
    }
}
