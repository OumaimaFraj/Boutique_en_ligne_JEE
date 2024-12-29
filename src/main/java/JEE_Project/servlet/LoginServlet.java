package JEE_Project.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import JEE_Project.connection.DBconnection;
import JEE_Project.dao.*;
import JEE_Project.model.*;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/user-login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendRedirect("login.jsp");
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html;charset=UTF-8");
		try (PrintWriter out = response.getWriter()) {
			String name = request.getParameter("login-name");
			String email = request.getParameter("login-email");
			String password = request.getParameter("login-password");
			
            // Créer une instance de UserDao
			UserDao udao = new UserDao(DBconnection.getConnection());
            // Vérifier si l'utilisateur existe

			User user = udao.userLogin(name, email, password);
			if (user != null) {
                // Stocker l'utilisateur dans la session
				request.getSession().setAttribute("auth", user);

				// Vérifier le rôle de l'utilisateur
                if ("administrateur".equalsIgnoreCase(user.getRole())) {
                    response.sendRedirect("AdminIndex.jsp"); // Redirection pour l'administrateur
                } 
                else {
                    response.sendRedirect("index.jsp"); // Redirection pour l'utilisateur standard
                }
             } else {
				out.println("there is no user");
			}

		} catch (ClassNotFoundException|SQLException e) {
			e.printStackTrace();
		} 

	}

}
