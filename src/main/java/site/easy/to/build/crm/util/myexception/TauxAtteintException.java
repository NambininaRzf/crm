package site.easy.to.build.crm.util.myexception;

import site.easy.to.build.crm.entity.CustomerExpenses;

public class TauxAtteintException extends Exception {
    CustomerExpenses customerExpenses;

    public TauxAtteintException(String message) {
        super(message);
    }

    public TauxAtteintException(String message,CustomerExpenses expenses) {
        super(message);
        this.customerExpenses = expenses;
    }

    public CustomerExpenses getCustomerExpenses(){return this.customerExpenses;}
}
