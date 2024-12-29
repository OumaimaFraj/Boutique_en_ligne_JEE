package JEE_Project.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import JEE_Project.model.Cart;
import JEE_Project.model.User;

@WebServlet("/remove-from-cart")
public class RemoveFromCartServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            String id = request.getParameter("id");
            if (id != null) {
            	
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
                
            	if(cart_list !=null) {
            		for(Cart c:cart_list) {
            			if(c.getId()==Integer.parseInt(id)) {
            				cart_list.remove(cart_list.indexOf(c));
            				break;
            			}
            		}
                	response.sendRedirect("panier.jsp");

            	}

            	
            }
            else {
            	response.sendRedirect("panier.jsp");
            }
            
        }
    }
}
