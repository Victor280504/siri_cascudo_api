package model;

public class UserRole {

    private int idUser;
    private String role;

    public UserRole() {
    }

    public UserRole(int idUser, String role) {
        this.idUser = idUser;
        this.role = role;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getToString() {
        return "Id do Usuário: " + this.getIdUser() + " | Perfil: " + this.getRole();
    }
}