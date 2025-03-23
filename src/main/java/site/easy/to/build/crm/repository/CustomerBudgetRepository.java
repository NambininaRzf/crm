package site.easy.to.build.crm.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import site.easy.to.build.crm.entity.CustomerBudget;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CustomerBudgetRepository extends JpaRepository<CustomerBudget, Long> {

    // Trouver un budget par son ID
    CustomerBudget findById(long id);

    // Trouver tous les budgets d'un client donné
    List<CustomerBudget> findByCustomerCustomerId(int customerId);

    // Trouver tous les budgets d'un utilisateur donné
    List<CustomerBudget> findByUserId(int userId);

    // Trouver tous les budgets d'un client donné, triés par date décroissante
    List<CustomerBudget> findByCustomerCustomerIdOrderByCreatedAtDesc(int customerId, Pageable pageable);

    // Compter le nombre de budgets pour un client donné
    long countByCustomerCustomerId(int customerId);

    // Compter le nombre de budgets pour un utilisateur donné
    long countByUserId(int userId);

    @Query("SELECT COALESCE(SUM(cb.amount), 0) FROM CustomerBudget cb WHERE cb.customer.customerId = :customerId")
    BigDecimal getTotalBudgetByCustomerId(int customerId);
}
