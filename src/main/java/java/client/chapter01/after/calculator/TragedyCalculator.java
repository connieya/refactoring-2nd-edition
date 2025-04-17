package java.client.chapter01.after.calculator;

import java.client.chapter01.after.PerformanceCalculator;
import java.client.chapter01.data.Performance;
import java.client.chapter01.data.Play;

public class TragedyCalculator extends PerformanceCalculator {

    private Performance performance;
    private Play play;

    public TragedyCalculator(Performance performance, Play play) {
        super(performance, play);
    }

    @Override
    public int amount() {
       int result = 40000;
        if (performance.getAudience() > 30) {
            result += 1000 * (performance.getAudience() - 30);
        }
        return result;
    }
}
