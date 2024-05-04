package Entities;

public class feedback {
    private int id; // Primary key, auto-increment
    private int guide_id; // Foreign key to Guide
    private String comment;
    private double rating;  // Added rating field

    // Constructors
    public feedback() {}

    public feedback(int id, int guide_id, String comment, double rating) {
        this.id = id;
        this.guide_id = guide_id;
        this.comment = comment;
        this.rating=rating;
    }

    public feedback(int guide_id, String comment, double rating) {
        this.guide_id = guide_id;
        this.comment = comment;
        this.rating=rating;
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
    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
    @Override
    public String toString() {
        return "Feedback{" +
                "id=" + id +
                ", guide_id=" + guide_id +
                ", comment='" + comment + '\'' +
                ", rating=" + rating +  // Add the rating to the output
                '}';
    }

}
