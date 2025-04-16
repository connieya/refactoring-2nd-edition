package java.client.chapter01.after;

import java.client.chapter01.data.Performance;
import java.client.chapter01.data.Play;
import java.util.Map;

public class EnrichPerformance {
    private Play play;
    private int amount;
    private String playID;
    private int audience;
    private int volumeCredit;

    public EnrichPerformance(Performance performance , Map<String ,Play> plays) {
        this.playID = performance.getPlayID();
        this.audience = performance.getAudience();
        this.play = playFor(performance,plays);
        this.amount = amountFor(performance ,this.play);
        this.volumeCredit = volumeCreditsFor(performance , plays);
    }

    public int getVolumeCredit() {
        return volumeCredit;
    }

    public void setVolumeCredit(int volumeCredit) {
        this.volumeCredit = volumeCredit;
    }

    public Play getPlay() {
        return play;
    }

    public void setPlay(Play play) {
        this.play = play;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getPlayID() {
        return playID;
    }

    public void setPlayID(String playID) {
        this.playID = playID;
    }

    public int getAudience() {
        return audience;
    }

    public void setAudience(int audience) {
        this.audience = audience;
    }

    private static int amountFor(Performance aPerformance, Play play) {
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

    private static Play playFor(Performance aPerformance, Map<String, Play> plays) {
        return plays.get(aPerformance.getPlayID());
    }

    private static int volumeCreditsFor(Performance aPerformance , Map<String, Play> plays) {
        int volumeCredits = 0;
        volumeCredits += Math.max(aPerformance.getAudience() - 30, 0);

        if ("comedy".equals(playFor(aPerformance, plays).getType())) {
            volumeCredits += (int) Math.floor((double) aPerformance.getAudience() / 5);
        }

        return volumeCredits;
    }
}
