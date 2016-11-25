package ru.simplgroupp.persistence.reports.model;

/**
 *
 */
public class ManagerSearchModel {
    public ManagerSearchModel(int id, String username) {
        this.id = id;
        this.username = username;
    }

    private int id;
    private String username;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
