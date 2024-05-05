package Entities;

public class Favs {

    private int id;
    private int user;
    private int acc;

    public Favs(int user, int acc) {
        this.user = user;
        this.acc = acc;
    }

    public Favs(int id, int user, int acc) {
        this.id = id;
        this.user = user;
        this.acc = acc;
    }

    public Favs() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public int getAcc() {
        return acc;
    }

    public void setAcc(int acc) {
        this.acc = acc;
    }

    @Override
    public String toString() {
        return "Favs{" +
                "id=" + id +
                ", user=" + user +
                ", acc=" + acc +
                '}';
    }
}
