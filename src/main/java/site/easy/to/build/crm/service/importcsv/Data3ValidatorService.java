package site.easy.to.build.crm.service.importcsv;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.CustomerBudget;
import site.easy.to.build.crm.entity.User;
import site.easy.to.build.crm.model.importcsv.Data1Validator;
import site.easy.to.build.crm.model.importcsv.Data2Validator;
import site.easy.to.build.crm.model.importcsv.Data3Validator;
import site.easy.to.build.crm.service.customer.CustomerBudgetService;
import site.easy.to.build.crm.service.customer.CustomerService;
import site.easy.to.build.crm.service.user.UserService;
import site.easy.to.build.crm.util.AuthenticationUtils;

@Service
public class Data3ValidatorService {
    private final UserService userService;
    private final CustomerBudgetService customerBudgetService;
    private final AuthenticationUtils authenticationUtils;
     private final CustomerService customerService;

    @Autowired
    public Data3ValidatorService(UserService userService,CustomerBudgetService customerBudgetService,
            AuthenticationUtils authenticationUtils,CustomerService customerService){
        this.userService =userService;
        this.customerBudgetService =customerBudgetService;
        this.authenticationUtils =authenticationUtils;
        this.customerService =customerService;
    }
    
    public void insertData(Authentication authentication,Data3Validator data) throws Exception{
        int userId = authenticationUtils.getLoggedInUserId(authentication);
        User user = userService.findById(userId);
        try {
            Customer customer = customerService.findByEmail(data.getCustomerEmail());
            if (customer==null) {
                throw new Exception("customer not found");
            }
            CustomerBudget customerBudget = new CustomerBudget();
            customerBudget.setCreatedAt(LocalDateTime.now());
            customerBudget.setAmount(BigDecimal.valueOf(data.getBudget()));
            customerBudget.setDescription("Budget description");
            customerBudget.setUser(user);
            customerBudget.setCustomer(customer);
            customerBudgetService.save(customerBudget);
        } catch (Exception e) {
           throw e;
        }
    }

    public void insertListData(Authentication authentication,List<Data3Validator> data) throws Exception{
        try {
            for (Data3Validator data3Validator : data) {
                insertData(authentication, data3Validator);
            }
        } catch (Exception e) {
           throw e;
        }
    }
}
