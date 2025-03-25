package site.easy.to.build.crm.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

import org.hibernate.envers.Audited;

@Entity
@Table(name = "taux_storaged")
@Audited
public class TauxStorage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private int id;

    @Column(name = "taux_alerte", nullable = false)
    @NotNull(message = "Taux alerte is required")
    private double tauxAlerte;

    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt = LocalDateTime.now();

    // Constructeurs
    public TauxStorage() {}

    public TauxStorage(double tauxAlerte) {
        this.tauxAlerte = tauxAlerte;
        this.createdAt = LocalDateTime.now();
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getTauxAlerte() {
        return tauxAlerte;
    }

    public void setTauxAlerte(double tauxAlerte) {
        this.tauxAlerte = tauxAlerte;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
