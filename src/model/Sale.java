package model;

import java.sql.Date;

public class Sale {
    private int id;
    private Date date;
    private String paymentMethod;
    private int idUser;

    public Sale() {
    }

    public Sale(int id, Date date, String paymentMethod, int idUser) {
        this.id = id;
        this.date = date;
        this.paymentMethod = paymentMethod;
        this.idUser = idUser;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getToString() {
        return "Id: " + this.getId() + " | Data: " + this.getDate() + " | Método de Pagamento: " + this.paymentMethod
                + " | Código do Comprador: "
                + this.idUser;
    }

}
