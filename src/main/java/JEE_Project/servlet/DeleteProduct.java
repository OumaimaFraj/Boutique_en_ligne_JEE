package JEE_Project.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import JEE_Project.dao.ProductDao;
import JEE_Project.connection.DBconnection;

@WebServlet("/delete-product")
public class DeleteProduct extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Gérer la suppression du produit via GET
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Récupérer l'ID du produit depuis la requête
        String idParam = request.getParameter("id");

        if (idParam != null) {
            try {
                // Convertir l'ID en entier
                int productId = Integer.parseInt(idParam);

                // Essayer de récupérer la connexion et d'effectuer la suppression
                try {
                    // Créer une instance de ProductDao pour interagir avec la base de données
                    ProductDao productDao = new ProductDao(DBconnection.getConnection());

                    // Appeler la méthode deleteProduct() pour supprimer le produit
                    boolean isDeleted = productDao.deleteProduct(productId);

                    // Vérifier si la suppression a réussi
                    if (isDeleted) {
                        // Si la suppression réussie, rediriger vers la page d'administration
                        response.sendRedirect("AdminIndex.jsp");  // Page admin avec la liste des produits
                    } else {
                        // Si la suppression a échoué, renvoyer une erreur
                        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erreur lors de la suppression du produit");
                    }
                } catch (SQLException | ClassNotFoundException e) {
                    // Gérer les exceptions liées à la connexion à la base de données
                    e.printStackTrace();
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erreur de base de données");
                }
            } catch (NumberFormatException e) {
                // Gérer l'erreur si l'ID du produit n'est pas un entier valide
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de produit invalide");
            }
        } else {
            // Si l'ID n'est pas présent dans la requête
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID du produit requis");
        }
    }
}
