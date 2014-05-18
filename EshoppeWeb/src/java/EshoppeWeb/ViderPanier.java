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
 * @author Simon
 */
@WebServlet(name = "ViderPanier", urlPatterns = {"/viderpanier"})
public class ViderPanier extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    HttpSession session ;
    String erreur;
    String nomUsager;
    

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
        session = request.getSession();
        nomUsager = (String)session.getAttribute("Nom_Joueur");
        erreur = (String)session.getAttribute("Erreur");
       viderLePanier();
        response.sendRedirect("http://localhost:8080/eshoppeweb/catalogue");
        //PrintWriter out = response.getWriter();
        //UtilHtml.enteteHtml(out, "viderpanier");
        //out.println("<h1> FUCK MY LIFE </h1>");
        //UtilHtml.piedsDePage(out, session);
    }
    
    private void viderLePanier()
    {
        ConnectionOracle odc = new ConnectionOracle();
            odc.setConnection("kellylea", "oracle2");
            odc.connecter();
            
            try{
                CallableStatement stm = odc.getConnection().prepareCall("{call GESTION_PANIER.VIDERPANIER( ? )}");
                stm.setString(1, nomUsager);
                int nombreLigne = stm.executeUpdate();
                stm.close();
                if(nombreLigne == 0)
                {
                    session.setAttribute("Erreur",  "Le panier est déjà vide");
                }
            }
            catch(SQLException sqe){session.setAttribute("Erreur", sqe.getMessage()+ "\n");}
            finally{odc.deconnecter();}
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
