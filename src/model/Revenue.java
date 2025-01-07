package model;

public class Revenue {
    private int idIngredient;
    private int idProduct;
    private int quantity;

    public Revenue() {
    }

    public Revenue(int idIngredient, int idProduct, int quantity) {
        this.idIngredient = idIngredient;
        this.idProduct = idProduct;
        this.quantity = quantity;
    }

    public int getIdIngredient() {
        return idIngredient;
    }

    public void setIdIngredient(int idIngredient) {
        this.idIngredient = idIngredient;
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

    public String getToString() {
        return "Id do Ingrediente: " + this.getIdIngredient() + " | Id do produto: " + this.getIdProduct() +
                " | Quantidade: " + this.quantity;
    }
}
