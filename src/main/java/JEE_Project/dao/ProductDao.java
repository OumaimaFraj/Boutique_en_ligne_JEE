package JEE_Project.dao;

import java.util.*;
import java.sql.Connection;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import JEE_Project.model.*;


public class ProductDao {
    private Connection con;
    private String query;
    private PreparedStatement pst;
    private ResultSet rs;

    public ProductDao(Connection con) {
        this.con = con;
    }

    // Method to get all products
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<Product>();
        try {
            query = "select * from products";
            pst = this.con.prepareStatement(query);
            rs = pst.executeQuery();

            while (rs.next()) {
                Product row = new Product();
                row.setId(rs.getInt("id"));
                row.setNom(rs.getString("nom"));                // Match 'nom' column
                row.setDescription(rs.getString("description")); // Match 'description' column
                row.setPrix(rs.getBigDecimal("prix"));           // Match 'prix' column, using BigDecimal for precise price
                row.setImage(rs.getString("image"));   

                products.add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return products;
    }

    
    public List<Cart> getCartProducts(ArrayList<Cart> cartList) {
        List<Cart> products = new ArrayList<Cart>();
        try {
            if (cartList.size() > 0) {
                for (Cart item : cartList) {
                    query = "select * from products where id=?";
                    pst = this.con.prepareStatement(query);
                    pst.setInt(1, item.getId());
                    rs = pst.executeQuery();
                    while (rs.next()) {
                        Cart row = new Cart();
                        row.setId(rs.getInt("id"));
                        row.setNom(rs.getString("nom"));
                        row.setDescription(rs.getString("description"));
                        row.setPrix(rs.getBigDecimal("prix").multiply(new BigDecimal(item.getQuantity())));
                        row.setQuantity(item.getQuantity());
                        products.add(row);
                    }

                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return products;
    }
    
    
    // To calculate total price mtaa panier 

    public BigDecimal getTotalCartPrice(ArrayList<Cart> cartList) {
        BigDecimal sum = BigDecimal.ZERO; // Initialiser sum avec BigDecimal.ZERO
        try {
            if (cartList.size() > 0) {
                for (Cart item : cartList) {
                    query = "select prix from products where id=?";
                    pst = this.con.prepareStatement(query);
                    pst.setInt(1, item.getId());
                    rs = pst.executeQuery();
                    while (rs.next()) {
                        BigDecimal price = rs.getBigDecimal("prix"); // Récupérer le prix en BigDecimal
                        BigDecimal quantity = new BigDecimal(item.getQuantity()); // Convertir la quantité en BigDecimal
                        sum = sum.add(price.multiply(quantity)); // Utiliser la méthode add() et multiply()
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return sum;
    }

// ---------------------Method to add product by Admin----------------------------------------------
    // New method: addProduct
    public void addProduct(Product product) {
        try {
            query = "INSERT INTO products (nom, description, prix, image, stock) VALUES (?, ?, ?, ?, ?)";
            pst = this.con.prepareStatement(query);
            pst.setString(1, product.getNom());
            pst.setString(2, product.getDescription());
            pst.setBigDecimal(3, product.getPrix());
            pst.setString(4, product.getImage());
            pst.setInt(5, product.getStock());

            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
//-------------Methode to delete a product---------------------------------------------------------------------------------------
    public boolean deleteProduct(int id) {
        try {
            query = "DELETE FROM products WHERE id = ?";
            pst = this.con.prepareStatement(query);
            pst.setInt(1, id);
            int rowsAffected = pst.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression du produit : " + e.getMessage());
            return false;
        }
    }
//---------------methode to update a product-----------------------------------------------------------------------------
 // Méthode pour récupérer un produit par son ID
    public Product getProductById(int id) throws SQLException {
        Product product = null;
        String query = "SELECT * FROM products WHERE id = ?";
        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                product = new Product();
                product.setId(rs.getInt("id"));
                product.setNom(rs.getString("nom"));
                product.setDescription(rs.getString("description"));
                product.setPrix(rs.getBigDecimal("prix"));
                product.setImage(rs.getString("image"));
                product.setStock(rs.getInt("stock"));
            }
        }
        return product;
    }

    // Méthode pour mettre à jour un produit
    public boolean updateProduct(Product product) throws SQLException {
        String query = "UPDATE products SET nom = ?, description = ?, prix = ?, image = ?, stock = ? WHERE id = ?";
        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, product.getNom());
            pst.setString(2, product.getDescription());
            pst.setBigDecimal(3, product.getPrix());
            pst.setString(4, product.getImage());
            pst.setInt(5, product.getStock());
            pst.setInt(6, product.getId());
            int rowsAffected = pst.executeUpdate();
            return rowsAffected > 0;
        }
    }
    //--------------------------Get Stock de produit pour faire le controle de saisie des buttons----------------------------------------------
    public int getProductStock(int productId) {
        int stock = 0;
        String query = "SELECT stock FROM products WHERE id = ?";
        try (PreparedStatement pst = this.con.prepareStatement(query)) {
            pst.setInt(1, productId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                stock = rs.getInt("stock");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du stock : " + e.getMessage());
        }
        return stock;
    }


  
}
