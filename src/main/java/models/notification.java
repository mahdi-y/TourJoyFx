package models;

import java.time.LocalDateTime;

public class notification {
    private Integer fkUser;
    private Integer id;
    private String message;
    private boolean is_read;
    private LocalDateTime created_at;

    public notification(Integer id, String message, boolean is_read, LocalDateTime created_at) {
        this.id = id;
        this.message = message;
        this.is_read = is_read;
        this.created_at = created_at;
    }

    public notification() {

    }

    public notification(int id, String message, boolean isRead, LocalDateTime createdAt, int fkUser) {
        this.id = id;
        this.message = message;
        this.is_read = isRead;
        this.created_at = createdAt;
        this.fkUser = fkUser;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public Integer getFkUser() {
        return fkUser;
    }

    public void setFkUser(Integer fkUser) {
        this.fkUser = fkUser;
    }

    public boolean isIs_read() {
        return is_read;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean getIs_read() {
        return is_read;
    }

    public void setIs_read(boolean is_read) {
        this.is_read = is_read;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    @Override
    public String toString() {
        return
                 message

                + "      "  +created_at
                ;
    }
}
