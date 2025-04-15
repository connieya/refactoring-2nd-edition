package java.client.chapter01.after;

import java.client.chapter01.data.Performance;
import java.util.List;

public class StatementData {

    private String customer;
    private List<Performance> performances;

    public StatementData(String customer, List<Performance> performances) {
        this.customer = customer;
        this.performances = performances;
    }


    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public List<Performance> getPerformances() {
        return performances;
    }

    public void setPerformances(List<Performance> performances) {
        this.performances = performances;
    }
}
