package java.client.chapter01.after.calculator;

import java.client.chapter01.after.PerformanceCalculator;
import java.client.chapter01.data.Performance;
import java.client.chapter01.data.Play;

public class ComedyCalculator extends PerformanceCalculator {

    private Performance performance;
    private Play play;

    public ComedyCalculator(Performance performance, Play play) {
        super(performance, play);
    }

    @Override
    public int amount() {
        int result = 30000;
        if (performance.getAudience() > 20) {
            result += 10000 + 500 * (performance.getAudience() - 20);
        }
        result += 300 * performance.getAudience();
        return result;
    }

    @Override
    public int volumeCredits() {
        return super.volumeCredits()+ (int) Math.floor((double) performance.getAudience() / 5);

    }
}
