package site.easy.to.build.crm.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.easy.to.build.crm.entity.CustomerExpenses;

import java.util.List;

@Repository
public interface CustomerExpensesRepository extends JpaRepository<CustomerExpenses, Long> {

    // Trouver une dépense par son ID
    CustomerExpenses findById(long id);

    // Trouver toutes les dépenses d'un client donné
    List<CustomerExpenses> findByCustomerCustomerId(int customerId);

    // Trouver toutes les dépenses d'un utilisateur donné
    List<CustomerExpenses> findByUserId(int userId);

    // Trouver toutes les dépenses d'un client donné, triées par date décroissante
    List<CustomerExpenses> findByCustomerCustomerIdOrderByCreatedAtDesc(int customerId, Pageable pageable);

    // Compter le nombre de dépenses pour un client donné
    long countByCustomerCustomerId(int customerId);

    // Compter le nombre de dépenses pour un utilisateur donné
    long countByUserId(int userId);
}
