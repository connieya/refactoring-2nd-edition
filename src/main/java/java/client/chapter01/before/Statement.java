package java.client.chapter01.before;

import java.client.chapter01.data.Invoice;
import java.client.chapter01.data.Performance;
import java.client.chapter01.data.Play;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

public class Statement {

    public String statement(Invoice invoice , Map<String , Play> plays) {
        int totalAmount = 0;
        int volumeCredits = 0;
        StringBuilder result = new StringBuilder("청구 내역 (고객명 : " + invoice.getCustomer() + ")\n");
        NumberFormat format =  NumberFormat.getCurrencyInstance(Locale.US);

        for (Performance performance : invoice.getPerformances()) {
            Play play = plays.get(performance.getPlayID());
            int thisAmount = 0;

            switch (play.getType()) {
                case "tragedy":
                    thisAmount = 40000;
                    if (performance.getAudience() > 30) {
                        thisAmount += 1000 * (performance.getAudience() - 30);
                    }
                    break;
                case "comedy" :
                    thisAmount = 30000;
                    if (performance.getAudience() > 20) {
                        thisAmount += 10000 + 500 *(performance.getAudience() - 20);
                    }
                    thisAmount += 300 * performance.getAudience();
                    break;
                default:
                    throw new IllegalArgumentException("알 수 없는 장르: " + play.getType());
            }
            volumeCredits += Math.max(performance.getAudience() - 30 , 0);

            if ("comedy".equals(play.getType())) {
                volumeCredits += Math.floor(performance.getAudience() / 5);
            }

            result.append(
                    String.format(
                            " %s : %s원 (%d석) \n",
                            play.getName(),
                            format.format(thisAmount / 100.0),
                            performance.getAudience()
                    )
            );

            totalAmount += thisAmount;

        }

        result.append(String.format("총액 : %s원\n" , format.format(totalAmount / 100.0)));
        result.append(String.format("적립 포인트 : %d점\n", volumeCredits));

        return result.toString();

    }
}
