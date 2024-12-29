package JEE_Project.servlet;  

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.*;
import java.text.SimpleDateFormat;
import java.math.BigDecimal;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import JEE_Project.connection.DBconnection;
import JEE_Project.dao.*;
import JEE_Project.model.*;

@WebServlet("/order-now")
public class OrderNowServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {
            // Récupérer la session et l'utilisateur authentifié
            HttpSession session = request.getSession();
            User authUser = (User) session.getAttribute("auth");

            // Vérifier si l'utilisateur est connecté
            if (authUser == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            // Récupérer le contenu du panier (List<Cart>)
            @SuppressWarnings("unchecked")
            ArrayList<Cart> cartList = (ArrayList<Cart>) session.getAttribute("cart-list-" + authUser.getId());

            // Vérifier si le panier n'est pas vide
            if (cartList == null || cartList.isEmpty()) {
                response.sendRedirect("panier.jsp");
                out.println("<h3>Panier vide ou non récupéré correctement.</h3>");
                return;
            }

            // Créer une commande avec les détails de l'utilisateur
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date currentDate = new Date();

            Order order = new Order();
            order.setUtilisateurId(authUser.getId());
            order.setDate(currentDate);
            order.setStatus("en cours");

            // Insérer la commande dans la base de données
            OrderDao orderDao = new OrderDao(DBconnection.getConnection());
            int orderId = orderDao.insertOrderAndGetId(order);
            if (orderId > 0) {
                // Insérer les détails des produits dans order_details
                boolean allDetailsInserted = true;
                System.out.println("Contenu du panier (cartList) : " + cartList);

                for (Cart cartItem : cartList) {
                    // Calcul du total pour le produit
                    BigDecimal prix = cartItem.getPrix(); // Prix unitaire en BigDecimal
                    BigDecimal quantite = new BigDecimal(cartItem.getQuantity()); // Quantité en BigDecimal
                    BigDecimal totalPrix = prix.multiply(quantite); // Total = prix * quantité

                    // Insertion dans la table des détails de commande
                    boolean detailInserted = orderDao.insertOrderDetails(
                            orderId, // id commande
                            cartItem.getId(), // id produit
                            cartItem.getQuantity(),
                            totalPrix);

                    if (!detailInserted) {
                        allDetailsInserted = false;
                        break;
                    }
                }

                // Si tous les détails ont été insérés, vider le panier
                if (allDetailsInserted) {
                    session.removeAttribute("cart-list-" + authUser.getId());
                    response.sendRedirect("commandes.jsp");
                } else {
                    out.println("<h3>Erreur lors de l'insertion des détails de la commande.</h3>");
                }
            } else {
                out.println("<h3>Erreur lors de la création de la commande.</h3>");
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            response.getWriter().println("<h3>Une erreur est survenue : " + e.getMessage() + "</h3>");
        }
    }
}
