package JEE_Project.model;
import java.math.BigDecimal;


public class Product {
    private int id;
    private String nom;             // Matching the 'nom' column in the table
    private String description;     // Matching the 'description' column in the table
    private BigDecimal prix;        // Matching the 'prix' column in the table, use BigDecimal for precise monetary values
    private String image;           // Matching the 'image' column in the table
    private int stock;              // Matching the 'stock' column in the table

    // Default constructor
    public Product() {
    }

    // Parameterized constructor
    public Product(int id, String nom, String description, BigDecimal prix, String image, int stock) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.prix = prix;
        this.image = image;
        this.stock = stock;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrix() {
        return prix;
    }

    public void setPrix(BigDecimal prix) {
        this.prix = prix;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "Product [id=" + id + ", nom=" + nom + ", description=" + description + ", prix=" + prix + ", image="
                + image + ", stock=" + stock + "]";
    }
}
