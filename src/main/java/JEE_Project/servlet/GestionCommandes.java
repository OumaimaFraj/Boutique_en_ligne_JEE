package JEE_Project.servlet;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import JEE_Project.connection.DBconnection;
import JEE_Project.dao.OrderDao;

@WebServlet("/gestion-commandes")
public class GestionCommandes extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Initialiser la connexion à la base de données
            Connection connection = DBconnection.getConnection();
            OrderDao orderDao = new OrderDao(connection);

            // Récupérer le paramètre action
            String action = request.getParameter("action");

            if (action != null && action.equals("ModifierStatus")) {
                // Récupérer l'ID de la commande
                int orderId = Integer.parseInt(request.getParameter("id"));

                // Récupérer le nouveau statut (par exemple, depuis un formulaire ou une valeur fixe)
                String newStatus = request.getParameter("status");
                if (newStatus == null || newStatus.isEmpty()) {
                    newStatus = "livré";
                }

                // Mettre à jour le statut de la commande dans la base de données
                boolean updated = orderDao.updateOrderStatus(orderId, newStatus);

                if (updated) {
                    // Ajouter un message de succès à la requête
                    request.setAttribute("successMessage", "Statut de la commande mis à jour avec succès !");
                } else {
                    // Ajouter un message d'erreur si la mise à jour échoue
                    request.setAttribute("errorMessage", "Échec de la mise à jour du statut de la commande.");
                }

                // Rediriger vers la page des commandes
                response.sendRedirect("gestion-commandes");
                return;
            }

            // Récupérer toutes les commandes
            request.setAttribute("orders", orderDao.getAllOrders());

            // Rediriger vers la page JSP des commandes
            request.getRequestDispatcher("GestionCommandes.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erreur interne.");
        }
    }
}
