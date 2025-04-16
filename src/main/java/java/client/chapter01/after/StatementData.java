package java.client.chapter01.after;

import java.client.chapter01.data.Invoice;
import java.client.chapter01.data.Performance;
import java.client.chapter01.data.Play;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StatementData {

    private String customer;
    private List<EnrichPerformance> performances;
    private int totalAmount;
    private int totalVolumeCredits;

    public StatementData(String customer, List<EnrichPerformance> performances, int totalAmount, int totalVolumeCredits) {
        this.customer = customer;
        this.performances = performances;
        this.totalAmount = totalAmount;
        this.totalVolumeCredits = totalVolumeCredits;
    }

    public static StatementData createStatementData(Invoice invoice , Map<String , Play> plays) {
        List<EnrichPerformance> performances = invoice.getPerformances().stream()
                .map(performance -> new EnrichPerformance(performance, plays) )
                .collect(Collectors.toList());

       return new StatementData(
               invoice.getCustomer()
               , performances
               , totalAmount(performances)
               , totalVolumeCredits(performances));
    }

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

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getTotalVolumeCredits() {
        return totalVolumeCredits;
    }

    public void setTotalVolumeCredits(int totalVolumeCredits) {
        this.totalVolumeCredits = totalVolumeCredits;
    }

    private static int totalVolumeCredits(List<EnrichPerformance> performances) {
        return performances.stream()
                .mapToInt(EnrichPerformance::getVolumeCredit)
                .sum();
    }

    private static int totalAmount(List<EnrichPerformance> performances) {
        return performances.stream()
                .mapToInt(EnrichPerformance::getVolumeCredit)
                .sum();
    }



}
