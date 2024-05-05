package models;

import java.time.LocalDateTime;

public class claims {
    private Integer id;
    private String title;
    private String description;
    private LocalDateTime createDate;
    private String state;
    private Integer fkC;

    private String reply;
    // Default constructor
    public claims() {
    }

    public claims(String title, String description, LocalDateTime createDate, String state, Integer fkC, String reply) {
        this.title = title;
        this.description = description;
        this.createDate = createDate;
        this.state = state;
        this.fkC = fkC;
        this.reply = reply;
    }

    // Parameterized constructor
    public claims(Integer id, String title, String description, LocalDateTime createDate, String state,Integer fkC, String reply) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.createDate = createDate;
        this.state = state;
        this.fkC = fkC;
        this.reply = reply;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
    public Integer getFkC() {
        return fkC;
    }

    public void setFkC(Integer fkC) {
        this.fkC = fkC;
    }


    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }
    @Override
    public String toString() {
        return "Claims{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", createDate=" + createDate +
                ", state='" + state + '\'' +
                ", fkC=" + fkC +
                ", reply='" + reply + '\'' +
                '}';
    }
}
