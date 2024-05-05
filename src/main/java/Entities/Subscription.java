package Entities;

public class Subscription {

    private Long id;
    private String plan;
    private int duration;
    private Transport typeT;

    public Subscription(long id, String plan, int duration, Transport typeT) {
        this.id = id;
        this.plan = plan;
        this.duration = duration;
        this.typeT = typeT;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Transport getTypeT() {
        return typeT;
    }

    public void setTypeT(Transport typeT) {
        this.typeT = typeT;
    }
}