package JEE_Project.dao;

import java.sql.*;
import JEE_Project.model.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
	private Connection con;

	private String query;
    private PreparedStatement pst;
    private ResultSet rs;

	public UserDao(Connection con) {
		this.con = con;
	}
	
	public User userLogin(String nom, String motDePasse, String email) {
		User user = null;
        try {
            query = "SELECT * FROM utilisateur WHERE nom = ? AND email = ? AND mot_de_passe = ?";
            pst = this.con.prepareStatement(query);
            pst.setString(1, nom);
            pst.setString(2, motDePasse);
            pst.setString(3, email);
            rs = pst.executeQuery();
            
            if(rs.next()){
            	user = new User();
            	 user.setId(rs.getInt("ID")); // Récupère l'ID
                 user.setNom(rs.getString("nom")); // Récupère le nom
                 user.setEmail(rs.getString("email")); // Récupère l'email
                 user.setMotDePasse(rs.getString("mot_de_passe")); // Récupère le mot de passe
                 user.setRole(rs.getString("role")); // Assurez-vous que le rôle est bien récupéré

            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la connexion de l'utilisateur : " + e.getMessage());
        }
        return user;
    }
	
	// Méthode pour enregistrer un utilisateur dans la base de données
    public boolean userRegister(User user) throws SQLException {
        String query = "INSERT INTO utilisateur (nom, email, mot_de_passe) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, user.getNom());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getMotDePasse());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
  //----------------------------------Afficher les utilisateurs au admin-------------------------------------------------
 // 1. Récupérer la liste des utilisateurs avec rôle "utilisateur"
    public List<User> getUtilisateurs() {
        List<User> utilisateurs = new ArrayList<>();
        String query = "SELECT * FROM utilisateur WHERE role = 'utilisateur'";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            rs = stmt.executeQuery();
            while (rs.next()) {
                User utilisateur = new User();
                utilisateur.setId(rs.getInt("ID"));
                utilisateur.setNom(rs.getString("nom"));
                utilisateur.setEmail(rs.getString("email"));
                utilisateur.setMotDePasse(rs.getString("mot_de_passe"));
                utilisateur.setRole(rs.getString("role"));
                utilisateurs.add(utilisateur);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des utilisateurs : " + e.getMessage());
        }
        return utilisateurs;
    }
   //-----------------------------Supprimer un utilisateur-------------------------------------------
    // 2. Supprimer un utilisateur par son ID
 // Supprimer un utilisateur et ses commandes associées
    public boolean supprimerUtilisateur(int id) {
        String deleteOrdersQuery = "DELETE FROM orders WHERE utilisateur_id = ?";
        String deleteUserQuery = "DELETE FROM utilisateur WHERE ID = ?";

        try (PreparedStatement deleteOrdersStmt = con.prepareStatement(deleteOrdersQuery);
             PreparedStatement deleteUserStmt = con.prepareStatement(deleteUserQuery)) {

            // Supprimer les commandes associées à l'utilisateur
            deleteOrdersStmt.setInt(1, id);
            deleteOrdersStmt.executeUpdate();

            // Supprimer l'utilisateur
            deleteUserStmt.setInt(1, id);
            return deleteUserStmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de l'utilisateur : " + e.getMessage());
        }
        return false;
    }

    //-----------------bloquer un utilisateur-----------------------------------------------------
    public boolean bloquerUtilisateur(int id) {
        String query = "UPDATE utilisateur SET role = 'bloque' WHERE ID = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erreur lors du blocage de l'utilisateur : " + e.getMessage());
        }
        return false;
    }
    //--------------------------------------------------------------------------------------
}
