package model;

public class Product {
    private int id;
    private String name;
    private float price;
    private int stock;

    /**
     * Creates a new instance of Product.
     * @param id valid integer.
     * @param name not null.
     * @param price valid float greater than 0.
     * @param stock valid integer greater than 0.
     */

    public Product(int id, String name, float price, int stock){
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public Product()
    {
        this.id=0;
        this.name=null;
        this.price=0.0f;
        this.stock=0;
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

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String toString() {
        return "" + name + ", price: " + price+", id: "+id;
    }
}
