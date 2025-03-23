package site.easy.to.build.crm.util.myexception;

import site.easy.to.build.crm.entity.CustomerExpenses;

public class DepassementException extends Exception{
    CustomerExpenses customerExpenses;

    public DepassementException(String message) {
        super(message);
    }

    public DepassementException(String message,CustomerExpenses expenses) {
        super(message);
        this.customerExpenses = expenses;
    }

    public CustomerExpenses getCustomerExpenses(){return this.customerExpenses;}
}
