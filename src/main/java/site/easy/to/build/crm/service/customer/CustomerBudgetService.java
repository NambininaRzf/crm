package site.easy.to.build.crm.service.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.easy.to.build.crm.entity.CustomerBudget;
import site.easy.to.build.crm.repository.CustomerBudgetRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerBudgetService implements CustomerBudgetServiceInterface {

    @Autowired
    private CustomerBudgetRepository customerBudgetRepository;

    @Override
    public CustomerBudget save(CustomerBudget customerBudget) {
        return customerBudgetRepository.save(customerBudget);
    }

    @Override
    public Optional<CustomerBudget> findById(Long id) {
        return customerBudgetRepository.findById(id);
    }

    @Override
    public List<CustomerBudget> findByCustomerCustomerId(int customerId) {
        return customerBudgetRepository.findByCustomerCustomerId(customerId);
    }

    @Override
    public List<CustomerBudget> findByUserId(int userId) {
        return customerBudgetRepository.findByUserId(userId);
    }

    @Override
    public List<CustomerBudget> findByCustomerCustomerIdOrderByCreatedAtDesc(int customerId, Pageable pageable) {
        return customerBudgetRepository.findByCustomerCustomerIdOrderByCreatedAtDesc(customerId, pageable);
    }

    @Override
    public long countByCustomerCustomerId(int customerId) {
        return customerBudgetRepository.countByCustomerCustomerId(customerId);
    }

    @Override
    public long countByUserId(int userId) {
        return customerBudgetRepository.countByUserId(userId);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        customerBudgetRepository.deleteById(id);
    }
}
