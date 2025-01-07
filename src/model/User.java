package model;

import java.util.List;

public class User {
    private int id;
    private String name;
    private String email;
    private String password;
    private String address;
    private String login;
    private List<String> roles;

    public User() {
    }

    public User(int id, String name, String email, String passoword, String address, String login, List<String> roles) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = passoword;
        this.address = address;
        this.login = login;
        this.roles = roles;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getToString() {
        return "Id: " + this.getId() + " | Name: " + this.getName() + " | Login: " + this.login + " | Email: "
                + this.email + " | Endereço: " + this.address;
    }
}