package Entities;

public class Transport {
    private int id;
    private String type_t;

    public Transport(Integer id, String type_t) {
        this.id = id;
        this.type_t = type_t;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType_t() {
        return type_t;
    }

    public void setType_t(String type_t) {
        this.type_t = type_t;
    }


}