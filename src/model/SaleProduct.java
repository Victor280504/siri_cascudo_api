package model;

public class SaleProduct {
    private int idUser;
    private int idProduct;
    private int quantity;
    private double value;

    public SaleProduct() {
    }

    public SaleProduct(int idUser, int idProduct, int quantity, double value) {
        this.idUser = idUser;
        this.idProduct = idProduct;
        this.quantity = quantity;
        this.value = value;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public int getQuantity() {
        return quantity;
    }

    public void seQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getToString() {
        return "Id do Usuário: " + this.getIdUser() + " | Id do produto: " + this.getIdProduct() +
                " | Quantidade: " + this.quantity + " | Preço: " + this.value;
    }
}
