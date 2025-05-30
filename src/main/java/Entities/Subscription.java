package Entities;

public class Subscription {

    private Long id;
    private String plan;
    private int duration;
    private Transport typeT;

    private int userId;

    public Subscription() {

    }

    public Subscription(int id, String plan, int duration) {
        this.id= (long) id;
        this.plan=plan;
        this.duration=duration;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Subscription(long id, String plan, int duration, Transport typeT) {
        this.id = id;
        this.plan = plan;
        this.duration = duration;
        this.typeT = typeT;
    }

    public Subscription(String plan, int duration, Transport typeT, int userId) {
        this.plan = plan;
        this.duration = duration;
        this.typeT = typeT;
        this.userId = userId;
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