package model;

public class Product {
    private int id;
    private String description;
    private String image;
    private int quantity;
    private double price;
    private int idCategory;

    public Product() {
    }

    public Product(int id, String description, String image, int quantity, double price, int idCategory) {
        this.id = id;
        this.description = description;
        this.image = image;
        this.quantity = quantity;
        this.price = price;
        this.idCategory = idCategory;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public int getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(int idCategory) {
        this.idCategory = idCategory;
    }

    public String getToString() {
        return "Id: " + this.getId() + " | Descrição: " + this.getDescription() + " | Imagem: "
                + this.image + " | Quantidade: " + this.quantity + " | Preço: " + this.price;
    }
}
