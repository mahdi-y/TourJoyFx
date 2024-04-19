package entities;

public class feedback {
    private int id; // Primary key, auto-increment
    private int guide_id; // Foreign key to Guide
    private String comment;

    // Constructors
    public feedback() {}

    public feedback(int id, int guide_id, String comment) {
        this.id = id;
        this.guide_id = guide_id;
        this.comment = comment;
    }

    public feedback(int guide_id, String comment) {
        this.guide_id = guide_id;
        this.comment = comment;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGuide_id() {
        return guide_id;
    }

    public void setGuide_id(int guide_id) {
        this.guide_id = guide_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "id=" + id +
                ", guide_id=" + guide_id +
                ", comment='" + comment + '\'' +
                '}';
    }
}
