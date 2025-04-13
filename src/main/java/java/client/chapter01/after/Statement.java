package java.client.chapter01.after;

import java.client.chapter01.data.Invoice;
import java.client.chapter01.data.Performance;
import java.client.chapter01.data.Play;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

public class Statement {

    public String statement(Invoice invoice, Map<String, Play> plays) {
        StringBuilder result = new StringBuilder("청구 내역 (고객명 : " + invoice.getCustomer() + ")\n");

        for (Performance performance : invoice.getPerformances()) {
            // 청구 내역을 출력한다.
            result.append(
                    String.format(
                            " %s : %s원 (%d석) \n",
                            playFor(performance, plays).getName(),
                            usd( amountFor(performance, playFor(performance, plays))),
                            performance.getAudience()
                    )
            );
        }

        result.append(String.format("총액 : %s원\n", usd(totalAmount(invoice, plays ))));
        result.append(String.format("적립 포인트 : %d점\n", totalVolumeCredits(invoice,plays)));

        return result.toString();

    }

    private int amountFor(Performance aPerformance, Play play) {
        int result = 0;

        switch (play.getType()) {
            case "tragedy":
                result = 40000;
                if (aPerformance.getAudience() > 30) {
                    result += 1000 * (aPerformance.getAudience() - 30);
                }
                break;
            case "comedy":
                result = 30000;
                if (aPerformance.getAudience() > 20) {
                    result += 10000 + 500 * (aPerformance.getAudience() - 20);
                }
                result += 300 * aPerformance.getAudience();
                break;
            default:
                throw new IllegalArgumentException("알 수 없는 장르: " + play.getType());
        }

        return result;
    }

    private Play playFor(Performance aPerformance, Map<String, Play> plays) {
        return plays.get(aPerformance.getPlayID());
    }

    private int volumeCreditsFor(Performance aPerformance , Map<String, Play> plays) {
        int volumeCredits = 0;
        volumeCredits += Math.max(aPerformance.getAudience() - 30, 0);

        if ("comedy".equals(playFor(aPerformance, plays).getType())) {
            volumeCredits += (int) Math.floor((double) aPerformance.getAudience() / 5);
        }

        return volumeCredits;
    }

    private String usd(final int aNumber) {
        final NumberFormat format = NumberFormat.getCurrencyInstance(Locale.US);

        return format.format(aNumber / 100.0);
    }

    private int totalVolumeCredits(Invoice invoice , Map<String, Play> plays) {
        int volumeCredits = 0;
        for (Performance performance : invoice.getPerformances()) {
            volumeCredits += volumeCreditsFor(performance,plays);
        }

        return volumeCredits;
    }

    private int totalAmount(Invoice invoice , Map<String, Play> plays) {
        int result = 0;
        for (Performance performance : invoice.getPerformances()) {
            result += amountFor(performance, playFor(performance, plays))
        }

        return result;
    }
}
