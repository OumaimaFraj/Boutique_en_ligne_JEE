package JEE_Project.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import JEE_Project.model.Cart;
import JEE_Project.model.User;
import JEE_Project.connection.DBconnection;
import JEE_Project.dao.*;
import JEE_Project.model.*;



@WebServlet("/quantity-inc-dec")
public class QuantityIncDecServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		response.setContentType("text/html;charset=UTF-8");
		try (PrintWriter out = response.getWriter()) {
			String action = request.getParameter("action");
			int id = Integer.parseInt(request.getParameter("id"));
			// Vérifier si un utilisateur est connecté
            HttpSession session = request.getSession();
            User authUser = (User) session.getAttribute("auth");
            
            if (authUser == null) {
                // Si aucun utilisateur n'est connecté, rediriger vers la page de connexion
                response.sendRedirect("login.jsp");
                return;
            }
            
         // Récupérer ou créer le panier pour l'utilisateur connecté
            @SuppressWarnings("unchecked")
            ArrayList<Cart> cart_list = (ArrayList<Cart>) session.getAttribute("cart-list-" + authUser.getId());
            
			if (action != null && id >= 1) {
				ProductDao productDao = new ProductDao(DBconnection.getConnection());
                Product product = productDao.getProductById(id);
				if (action.equals("inc")) {
					for (Cart c : cart_list) {
						if (c.getId() == id) {
							// Vérifiez si la quantité ajoutée ne dépasse pas le stock disponible
							if (c.getQuantity() < product.getStock()) {
                                c.setQuantity(c.getQuantity() + 1);
                            } else {
                                session.setAttribute("errorMessage", "La quantité demandée dépasse le stock disponible.");
                            }
							response.sendRedirect("panier.jsp");
						}
					}
				}

				if (action.equals("dec")) {
					for (Cart c : cart_list) {
						if (c.getId() == id && c.getQuantity() > 1) {
							int quantity = c.getQuantity();
							quantity--;
							c.setQuantity(quantity);
							break;
						}
					}
					response.sendRedirect("panier.jsp");
				}
			} else {
				response.sendRedirect("panier.jsp");
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}