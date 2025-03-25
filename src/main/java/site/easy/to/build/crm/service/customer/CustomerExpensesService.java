package site.easy.to.build.crm.service.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.CustomerExpenses;
import site.easy.to.build.crm.entity.TauxStorage;
import site.easy.to.build.crm.repository.CustomerExpensesRepository;
import site.easy.to.build.crm.service.taux.TauxStorageService;
import site.easy.to.build.crm.util.myexception.DepassementException;
import site.easy.to.build.crm.util.myexception.TauxAtteintException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerExpensesService {

    
    private CustomerExpensesRepository customerExpensesRepository;
    private CustomerServiceImpl customerServiceImpl;
    private TauxStorageService tauxStorageService;

    @Autowired
    public CustomerExpensesService (CustomerExpensesRepository customerExpensesRepository,CustomerServiceImpl customerServiceImpl,
    TauxStorageService tauxStorageService){
        this.customerExpensesRepository =customerExpensesRepository;
        this.customerServiceImpl =customerServiceImpl;
        this.tauxStorageService =tauxStorageService;
    }

    public void expensesValidation(CustomerExpenses customerExpenses) throws Exception{
        Customer customer = customerExpenses.getCustomer();
        System.out.println("CUSTOMER ID  HERE:" +customer.getCustomerId());
        BigDecimal budget = customerServiceImpl.getTotalBudgetByCustomerId(customer);
        BigDecimal expenses = customerServiceImpl.getTotalExpensesByCustomerId(customer);
        double totaExpenses = expenses.doubleValue() + customerExpenses.getAmount().doubleValue();
        TauxStorage latest = tauxStorageService.getDefault();
        double taux = latest.getTauxAlerte();
        double totalBudget = budget.doubleValue();
        double seuil = totalBudget * taux/100;
        System.out.println("CUSTOMER BUDGET:" +totalBudget);
        System.out.println("CUSTOMER EXP:" +totaExpenses);
        System.out.println("TAUX ALERTE:" +taux);
        System.out.println("SEUIL:" +seuil);

        if (totaExpenses>= seuil && totaExpenses <= totalBudget) {
            throw new TauxAtteintException("Le taux d'alerte sur le budget vient d'être atteint");
        }else if (totaExpenses>totalBudget) {
            throw new DepassementException("Les depenses du customer ont dépassé le budget");
        }


    }

    public CustomerExpenses insertExpenses(CustomerExpenses customerExpenses) throws Exception {
        try {
            expensesValidation(customerExpenses);
        } catch (TauxAtteintException e) {
            confirm(customerExpenses);
            throw new TauxAtteintException(e.getMessage(), customerExpenses);
        }catch(DepassementException ex){
            throw new DepassementException(ex.getMessage(), customerExpenses);
        }catch(Exception ext){ throw ext;}
        return confirm(customerExpenses);
        
    }

    /**
     * Enregistre une nouvelle dépense avec un statut de 0 (non confirmée)
     * @param customerExpenses La dépense à enregistrer
     * @return La dépense enregistrée
     */
    public CustomerExpenses save(CustomerExpenses customerExpenses) {
        customerExpenses.setStatus(0); // Statut initial = 0 (non confirmée)
        return customerExpensesRepository.save(customerExpenses);
    }

    /**
     * Confirme une dépense en changeant son statut à 1
     * @param customerExpenses La dépense à confirmer
     * @return La dépense confirmée
     */
    @Transactional
    public CustomerExpenses confirm(CustomerExpenses customerExpenses) {
        Optional<CustomerExpenses> existingExpense = customerExpensesRepository.findById(customerExpenses.getId());

        if (existingExpense.isPresent()) {
            CustomerExpenses expenseToConfirm = existingExpense.get();
            expenseToConfirm.setStatus(1); // Changer le statut à 1 (confirmée)
            return customerExpensesRepository.save(expenseToConfirm);
        } else {
            throw new IllegalArgumentException("Dépense non trouvée avec l'ID : " + customerExpenses.getId());
        }
    }

    public CustomerExpenses findById(long id){
        return customerExpensesRepository.findById(id);
    }

    public List<CustomerExpenses>  findAll(){
        return customerExpensesRepository.findAll(); 
    }
}
