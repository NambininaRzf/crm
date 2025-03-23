package site.easy.to.build.crm.service.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.easy.to.build.crm.entity.CustomerExpenses;
import site.easy.to.build.crm.repository.CustomerExpensesRepository;

import java.util.Optional;

@Service
public class CustomerExpensesService {

    @Autowired
    private CustomerExpensesRepository customerExpensesRepository;

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
}
