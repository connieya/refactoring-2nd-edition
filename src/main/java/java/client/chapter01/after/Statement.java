package java.client.chapter01.after;

import java.client.chapter01.data.Invoice;
import java.client.chapter01.data.Play;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

public class Statement {

    public String statement(Invoice invoice , Map<String , Play> plays) {
        StatementData statementData = StatementData.createStatementData(invoice, plays);
        return renderPlainText(statementData);

    }

    public String renderPlainText(StatementData data) {
        StringBuilder result = new StringBuilder("청구 내역 (고객명 : " + data.getCustomer() + ")\n");

        for (EnrichPerformance performance : data.getPerformances()) {
            // 청구 내역을 출력한다.
            result.append(
                    String.format(
                            " %s : %s원 (%d석) \n",
                            performance.getPlay().getName(),
                            usd( performance.getAmount()),
                            performance.getAudience()
                    )
            );
        }

        result.append(String.format("총액 : %s원\n", usd(data.getTotalAmount())));
        result.append(String.format("적립 포인트 : %d점\n", data.getTotalVolumeCredits()));

        return result.toString();

    }


    private String usd(final int aNumber) {
        final NumberFormat format = NumberFormat.getCurrencyInstance(Locale.US);

        return format.format(aNumber / 100.0);
    }


}
