package JEE_Project.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import JEE_Project.model.Order;
import JEE_Project.model.OrderDetails;
import java.math.BigDecimal;


public class OrderDao {
    private Connection con;

    public OrderDao(Connection con) {
        this.con = con;
    }

    /**
     * Insérer une commande et retourner son ID généré.
     *
     * @param order L'objet Order contenant les détails de la commande.
     * @return L'ID de la commande générée, ou -1 en cas d'échec.
     */
    public int insertOrderAndGetId(Order order) {
        int orderId = -1;
        String query = "INSERT INTO orders (utilisateur_id, date, status) VALUES (?, ?, ?)";

        try (PreparedStatement pst = this.con.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pst.setInt(1, order.getUtilisateurId());
            pst.setDate(2, new java.sql.Date(order.getDate().getTime()));
            pst.setString(3, order.getStatus());

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                // Récupérer l'ID généré
                try (ResultSet rs = pst.getGeneratedKeys()) {
                    if (rs.next()) {
                        orderId = rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'insertion de la commande : " + e.getMessage());
        }

        return orderId;
    }
    

    /**
     * Insérer les détails d'une commande dans order_details.
     *
     * @param commandeId L'ID de la commande.
     * @param produitId  L'ID du produit.
     * @param quantite   La quantité commandée.
     * @param prixTotal  Le prix total (quantité * prix unitaire).
     * @return true si l'insertion est réussie, false sinon.
     */
    
    
    public boolean insertOrderDetails(int commande_id, int produit_id, int quantite, BigDecimal prix_total) {
        boolean result = false;
        String checkStockQuery = "SELECT stock FROM products WHERE id = ?"; // Vérifie le stock du produit
        String insertOrderDetailsQuery = "INSERT INTO order_details (commande_id, produit_id, quantite, prix_total) VALUES (?, ?, ?, ?)";

        try (PreparedStatement checkStockPst = this.con.prepareStatement(checkStockQuery)) {
            // Vérifie le stock disponible
            checkStockPst.setInt(1, produit_id);
            ResultSet rs = checkStockPst.executeQuery();

            if (rs.next()) {
                int stockDisponible = rs.getInt("stock");

                // Vérifie si la quantité demandée est disponible
                if (stockDisponible >= quantite) {
                    // Insérer les détails de la commande
                    try (PreparedStatement insertOrderPst = this.con.prepareStatement(insertOrderDetailsQuery)) {
                        insertOrderPst.setInt(1, commande_id);
                        insertOrderPst.setInt(2, produit_id);
                        insertOrderPst.setInt(3, quantite);
                        insertOrderPst.setBigDecimal(4, prix_total);

                        int rowsAffected = insertOrderPst.executeUpdate();
                        result = rowsAffected > 0;
                    }

                    // Mettre à jour le stock après insertion réussie
                    if (result) {
                        String updateStockQuery = "UPDATE products SET stock = stock - ? WHERE id = ?";
                        try (PreparedStatement updateStockPst = this.con.prepareStatement(updateStockQuery)) {
                            updateStockPst.setInt(1, quantite);
                            updateStockPst.setInt(2, produit_id);
                            updateStockPst.executeUpdate();
                        }
                    }
                } else {
                    // Message si le stock est insuffisant
                    System.out.println("Stock insuffisant pour le produit ID " + produit_id + 
                        ". Quantité demandée : " + quantite + ", Stock disponible : " + stockDisponible);
                }
            } else {
                // Message si le produit n'existe pas
                System.out.println("Produit ID " + produit_id + " introuvable.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'insertion des détails de la commande : " + e.getMessage());
        }

        return result;
    }

    //------------------------------------------Pour l'affichage des commandes dans commandes.jsp
    //------------------------------------------Pour Gestion des commandes by admin---------------------------------------------------------
    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM orders";

        try (PreparedStatement pst = this.con.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Order order = new Order();
                order.setId(rs.getInt("id"));
                order.setUtilisateurId(rs.getInt("utilisateur_id"));
                order.setDate(rs.getDate("date"));
                order.setStatus(rs.getString("status"));
                orders.add(order);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des commandes : " + e.getMessage());
        }

        return orders;
    }
 //------------------------------------GetOrdersby Id user-----------------------------------------------------------
    public List<Order> getOrdersByUserId(int userId) {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM orders WHERE utilisateur_id = ?";
        try (PreparedStatement pst = this.con.prepareStatement(query)) {
            pst.setInt(1, userId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Order order = new Order();
                order.setId(rs.getInt("id"));
                order.setUtilisateurId(rs.getInt("utilisateur_id"));
                order.setDate(rs.getDate("date"));
                order.setStatus(rs.getString("status"));
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }
//---------------------------------------------afficher les order_details de chaque order pour un user --------------------------------------------------
    public List<OrderDetails> getOrderDetailsByOrderId(int orderId) {
        List<OrderDetails> details = new ArrayList<>();
        String query = "SELECT od.*, p.nom AS produit_nom " +
                       "FROM order_details od " +
                       "JOIN products p ON od.produit_id = p.id " +
                       "WHERE od.commande_id = ?";

        try (PreparedStatement pst = this.con.prepareStatement(query)) {
            pst.setInt(1, orderId);  // Set the order ID in the query
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                OrderDetails detail = new OrderDetails();
                detail.setProduitId(rs.getInt("produit_id"));
                detail.setCommandeId(orderId);
                detail.setQuantite(rs.getInt("quantite"));
                detail.setPrixTotal(rs.getBigDecimal("prix_total"));

                // Récupération du nom du produit
                detail.setProduitNom(rs.getString("produit_nom"));  // Assurez-vous que la classe OrderDetails a un champ pour le nom du produit

                details.add(detail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return details;
    }

//-----------------------------Pour modifier le status de commande by admin--------------------------
    // Méthode pour mettre à jour le statut d'une commande
    public boolean updateOrderStatus(int orderId, String newStatus) {
        String query = "UPDATE orders SET status = ? WHERE id = ?";
        try (PreparedStatement ps = this.con.prepareStatement(query)) {
            ps.setString(1, newStatus);
            ps.setInt(2, orderId);
            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0; // Retourne vrai si une ligne est mise à jour
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Retourne faux en cas d'erreur
        }
    }
    
}
