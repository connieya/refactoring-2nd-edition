package java.client.chapter01.after;

import java.client.chapter01.data.Performance;
import java.client.chapter01.data.Play;
import java.util.Map;

public class PerformanceCalculator {
    private final Performance performance;
    private final Play play;

    public PerformanceCalculator(Performance performance, Play play) {
        this.performance = performance;
        this.play = play;
    }

    public int amountFor() {
        int result = 0;

        switch (play.getType()) {
            case "tragedy":
                result = 40000;
                if (performance.getAudience() > 30) {
                    result += 1000 * (performance.getAudience() - 30);
                }
                break;
            case "comedy":
                result = 30000;
                if (performance.getAudience() > 20) {
                    result += 10000 + 500 * (performance.getAudience() - 20);
                }
                result += 300 * performance.getAudience();
                break;
            default:
                throw new IllegalArgumentException("알 수 없는 장르: " + play.getType());
        }

        return result;
    }

    public int volumeCreditsFor() {
        int volumeCredits = 0;
        volumeCredits += Math.max(performance.getAudience() - 30, 0);

        if ("comedy".equals(play.getType())) {
            volumeCredits += (int) Math.floor((double) performance.getAudience() / 5);
        }

        return volumeCredits;
    }
}
