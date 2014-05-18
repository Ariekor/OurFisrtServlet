/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package EshoppeWeb;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.accessibility.AccessibleRole;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.JOptionPane;

/**
 *
 * @author Isabelle
 */
@WebServlet(name = "login", urlPatterns = {"/login"})
public class login extends HttpServlet {

    private String nomUser;
    private String motDePasse;
    private HttpSession session;
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        
        session = request.getSession();// session ne sera jamais null
        
        //valider user
        nomUser = request.getParameter( "user" );
        motDePasse = request.getParameter("motdepasse");
        
        if (validerJoueur(nomUser, motDePasse))
        {
            // si valide, set.
            session.setAttribute( "Nom_Joueur", nomUser );
            response.sendRedirect("http://localhost:8080/eshoppeweb/catalogue");
        } 
        else 
        {
            //retourner à catalogue avec login non valide.
            session.setAttribute("Erreur","Désolé, nom d'usager ou  mot de passe non valide.");
            response.sendRedirect("http://localhost:8080/eshoppeweb/catalogue");
        }
        
    }
    

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private boolean validerJoueur(String nom, String mdp)
    {
        boolean valide = false;
        String sqlLogin = "select nomusager from joueursrpg where nomusager = '"+nom+"' and motdepasse = '"+mdp+"'";
        ConnectionOracle oradb = new ConnectionOracle();
        oradb.setConnection("kellylea", "oracle2");
        oradb.connecter(); 
        try
        {    
            Statement stm = oradb.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rst = stm.executeQuery(sqlLogin);
            if(rst.first())
            {
                valide = true;
            }
            rst.close();
            stm.close();            
        }
        catch (SQLException e){/*faire quelquechose ici*/} 
        finally{oradb.deconnecter();}
        
        return valide;
    }
}
