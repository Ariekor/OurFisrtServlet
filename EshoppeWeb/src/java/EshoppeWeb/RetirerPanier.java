/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package EshoppeWeb;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Isabelle
 */
@WebServlet(name = "RetirerPanier", urlPatterns = {"/retirerpanier"})
public class RetirerPanier extends HttpServlet {

    private HttpSession session;
    //à récupérer du cookie
    private String erreur = "";    
    private String nomUser;
    private String numItem;
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
        nomUser = (String)session.getAttribute( "Nom_Joueur" );        
        numItem = request.getParameter("numitem");
        
        retirerPanier(nomUser, numItem);        
        session.setAttribute("Erreur", erreur);
        response.sendRedirect("http://localhost:8080/eshoppeweb/panier");
        
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
        response.sendRedirect("http://localhost:8080/eshoppeweb/catalogue");
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
    
    private void retirerPanier(String nomUser, String numItem)
    {
        //GESTION_PANIER.SUPPRIMER(numItem,nomUsage)
        ConnectionOracle oradbRetirer = new ConnectionOracle();
            oradbRetirer.setConnection("kellylea", "oracle2");
            oradbRetirer.connecter();
            try{
                CallableStatement stm = oradbRetirer.getConnection().prepareCall("{call GESTION_PANIER.SUPPRIMER( ? , ? )}");
                stm.setInt(1, Integer.parseInt(numItem));
                stm.setString(2, nomUser);
                               
                int retrait = stm.executeUpdate();
                if (retrait == 0)
                {
                    erreur += "\n L'item n'a pas été retiré...\n ";
                }
                stm.close();                
            }
            catch(SQLException sqe){session.setAttribute("Erreur", sqe.getMessage()+ "\n");}
            finally{oradbRetirer.deconnecter();}     
        
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

}
