package model;

public class Ingredient {
    private int id;
    private String description;
    private int quantity;
    private double price;

    public Ingredient() {
    }

    public Ingredient(int id, String description, int quantity, double price) {
        this.id = id;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getToString() {
        return "Id: " + this.getId() + " | Descrição: " + this.getDescription() +
                " | Quantidade: " + this.quantity + " | Preço: " + this.price;
    }
}
