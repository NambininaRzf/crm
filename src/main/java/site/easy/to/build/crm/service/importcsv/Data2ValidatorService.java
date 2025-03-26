package site.easy.to.build.crm.service.importcsv;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.CustomerLoginInfo;
import site.easy.to.build.crm.entity.OAuthUser;
import site.easy.to.build.crm.entity.User;
import site.easy.to.build.crm.model.importcsv.Data1Validator;
import site.easy.to.build.crm.model.importcsv.Data2Validator;
import site.easy.to.build.crm.service.customer.CustomerLoginInfoService;
import site.easy.to.build.crm.service.customer.CustomerService;
import site.easy.to.build.crm.service.user.UserService;
import site.easy.to.build.crm.util.AuthenticationUtils;
import site.easy.to.build.crm.util.EmailTokenUtils;

@Service
public class Data2ValidatorService {
    public Data1Validator dataValidator;
    private final AuthenticationUtils authenticationUtils;
    private final CustomerService customerService;
    private final CustomerLoginInfoService customerLoginInfoService;
    private final UserService userService;

    @Autowired
    public Data2ValidatorService(AuthenticationUtils authenticationUtils,CustomerService customerService,
            CustomerLoginInfoService customerLoginInfoService,UserService userService){
        this.authenticationUtils = authenticationUtils;
        this.customerService = customerService;
        this.userService = userService;
        this.customerLoginInfoService = customerLoginInfoService;
    }


    public void insertData (Authentication authentication,Data2Validator data)throws Exception{
        int userId = authenticationUtils.getLoggedInUserId(authentication);
        User user = userService.findById(userId);

        try {
            // Customer
            Customer customer = new Customer();
            customer.setEmail(data.getCustomerEmail());
            customer.setName(data.getCustomerName());
            customer.setUser(user);
            customer.setCreatedAt(LocalDateTime.now());
            customer.setCountry("Madagascar");
            // customer_login_info
    
            CustomerLoginInfo customerLoginInfo = new CustomerLoginInfo();
            customerLoginInfo.setEmail(customer.getEmail());
            String token = EmailTokenUtils.generateToken();
            customerLoginInfo.setToken(token);
            customerLoginInfo.setPasswordSet(false);
    
            CustomerLoginInfo customerLoginInfo1 = customerLoginInfoService.save(customerLoginInfo);
            customer.setCustomerLoginInfo(customerLoginInfo1);
            Customer createdCustomer = customerService.save(customer);
            customerLoginInfo1.setCustomer(createdCustomer);
            
        } catch (Exception e) {
            throw e;
        }

    }
    public void insertListData(Authentication authentication,List<Data2Validator> data) throws Exception{
        try {
            for (Data2Validator data2Validator : data) {
                insertData(authentication, data2Validator);
            }
        } catch (Exception e) {
           throw e;
        }
    }
}
