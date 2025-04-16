package java.client.chapter01.after;

import java.client.chapter01.after.calculator.ComedyCalculator;
import java.client.chapter01.after.calculator.TragedyCalculator;
import java.client.chapter01.data.Performance;
import java.client.chapter01.data.Play;


public abstract class PerformanceCalculator {
    private final Performance performance;
    private final Play play;

    public PerformanceCalculator(Performance performance, Play play) {
        this.performance = performance;
        this.play = play;
    }

    public static PerformanceCalculator createPerformanceCalculator(Performance performance, Play play) {
        switch (play.getType()) {
            case "tragedy" :
                return new TragedyCalculator(performance,play);
            case "comedy" :
                return new ComedyCalculator(performance,play);
            default:
                throw new IllegalArgumentException("알 수 없는 장르: " + play.getType());
        }
    }

     protected abstract int amount();


    public int volumeCredits() {
        return Math.max(performance.getAudience() - 30, 0);
    }
}
