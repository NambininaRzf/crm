package site.easy.to.build.crm.service.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import site.easy.to.build.crm.repository.CustomerBudgetRepository;
import site.easy.to.build.crm.repository.CustomerExpensesRepository;
import site.easy.to.build.crm.repository.CustomerRepository;
import site.easy.to.build.crm.entity.Customer;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private CustomerBudgetRepository customerBudgetRepository;
    private CustomerExpensesRepository customerExpensesRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository,CustomerBudgetRepository customerBudgetRepository,CustomerExpensesRepository customerExpensesRepository) {
        this.customerRepository = customerRepository;
        this.customerBudgetRepository = customerBudgetRepository;
        this.customerExpensesRepository = customerExpensesRepository;
    }

    @Override
    public Customer findByCustomerId(int customerId) {
        return customerRepository.findByCustomerId(customerId);
    }

    @Override
    public Customer findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    @Override
    public List<Customer> findByUserId(int userId) {
        return customerRepository.findByUserId(userId);
    }

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public void delete(Customer customer) {
        customerRepository.delete(customer);
    }

    @Override
    public List<Customer> getRecentCustomers(int userId, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return customerRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }

    @Override
    public long countByUserId(int userId) {
        return customerRepository.countByUserId(userId);
    }

    @Override
    public BigDecimal getTotalBudgetByCustomerId(Customer customer) {
        return customerBudgetRepository.getTotalBudgetByCustomerId(customer.getCustomerId().intValue());
    }

    @Override
    public BigDecimal getTotalExpensesByCustomerId(Customer customer) {
        return customerExpensesRepository.getTotalExpensesByCustomerId(customer.getCustomerId().intValue());
    }
}
