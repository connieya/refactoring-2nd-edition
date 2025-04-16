package java.client.chapter01.after;

import java.client.chapter01.data.Performance;
import java.util.List;

public class StatementData {

    private String customer;
    private List<EnrichPerformance> performances;

    public StatementData(String customer, List<EnrichPerformance> performances) {
        this.customer = customer;
        this.performances = performances;
    }


    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public List<EnrichPerformance> getPerformances() {
        return performances;
    }

    public void setPerformances(List<EnrichPerformance> performances) {
        this.performances = performances;
    }
}
