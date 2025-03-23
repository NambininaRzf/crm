package site.easy.to.build.crm.service.customer;

import org.springframework.data.domain.Pageable;
import site.easy.to.build.crm.entity.CustomerBudget;

import java.util.List;
import java.util.Optional;

public interface CustomerBudgetServiceInterface {

    CustomerBudget save(CustomerBudget customerBudget);

    Optional<CustomerBudget> findById(Long id);

    List<CustomerBudget> findByCustomerCustomerId(int customerId);

    List<CustomerBudget> findByUserId(int userId);

    List<CustomerBudget> findByCustomerCustomerIdOrderByCreatedAtDesc(int customerId, Pageable pageable);

    long countByCustomerCustomerId(int customerId);

    long countByUserId(int userId);

    void deleteById(Long id);
}
