package site.easy.to.build.crm.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "customer_expenses",
       uniqueConstraints = {
           @UniqueConstraint(name = "unique_lead", columnNames = "lead_id"),
           @UniqueConstraint(name = "unique_ticket", columnNames = "ticket_id")
       })
public class CustomerExpenses {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "amount", nullable = false, precision = 15, scale = 2)
    @NotNull(message = "Amount is required")
    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonIgnoreProperties("customerExpenses")
    private Customer customer;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties("customerExpenses")
    private User user;

    @OneToOne
    @JoinColumn(name = "lead_id", unique = true)
    private Lead lead;

    @OneToOne
    @JoinColumn(name = "ticket_id", unique = true)
    private Ticket ticket;

    @Column(name = "status", nullable = false)
    private int status;

    public CustomerExpenses() {}

    public CustomerExpenses(BigDecimal amount, Customer customer, String description, User user, Lead lead, Ticket ticket, int status) {
        this.amount = amount;
        this.customer = customer;
        this.description = description;
        this.user = user;
        this.lead = lead;
        this.ticket = ticket;
        this.status = status;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Lead getLead() { return lead; }
    public void setLead(Lead lead) { this.lead = lead; }

    public Ticket getTicket() { return ticket; }
    public void setTicket(Ticket ticket) { this.ticket = ticket; }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    // @Override
    // public String toString() {
    //     return "CustomerExpenses{" +
    //             "id=" + id +
    //             ", createdAt=" + createdAt +
    //             ", amount=" + amount +
    //             ", customer=" + (customer != null ? customer.getCustomerId() : "null") +
    //             ", description='" + description + '\'' +
    //             ", user=" + (user != null ? user.getId() : "null") +
    //             ", lead=" + (lead != null ? lead.getId() : "null") +
    //             ", ticket=" + (ticket != null ? ticket.getId() : "null") +
    //             ", status=" + status +
    //             '}';
    // }
}
