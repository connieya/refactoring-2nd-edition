package java.client.chapter01.after;

import java.client.chapter01.data.Performance;
import java.client.chapter01.data.Play;

public class EnrichPerformance {
    private Play play;
    private int amount;
    private String playID;
    private int audience;
    private int volumeCredit;

    public EnrichPerformance(Performance performance , Play play , int amount , int volumeCredit) {
        this.play = play;
        this.playID = performance.getPlayID();
        this.audience = performance.getAudience();
        this.amount = amount;
        this.volumeCredit = volumeCredit;
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
}
