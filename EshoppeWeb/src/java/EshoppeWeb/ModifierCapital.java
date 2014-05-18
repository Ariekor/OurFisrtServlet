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
 * @author Léa
 */
@WebServlet(name = "ModifierCapital", urlPatterns = {"/modifiercapital"})
public class ModifierCapital extends HttpServlet {

   private HttpSession session;
   private String nomUser = "";
   private int capUser;
   private String erreur = "";
   
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
      
      session = request.getSession();
      nomUser = (String)session.getAttribute( "Nom_Joueur" );
      capUser = Integer.parseInt(request.getParameter("Capital"));
      erreur = (String)session.getAttribute("Erreur");
      modifCapital();
      response.sendRedirect("http://localhost:8080/eshoppeweb/profil");
   }
   
private void modifCapital()
    {
        ConnectionOracle odc = new ConnectionOracle();
        odc.setConnection("kellylea", "oracle2");
        odc.connecter();
        try
        {
            CallableStatement stm = odc.getConnection().prepareCall("{call GESTION_USERS.MODIFIERCAPITAL( ? , ? )}");
            stm.setString(1, nomUser);
            stm.setInt(2, capUser);
            int modifie = stm.executeUpdate();
            if (modifie == 0)
            {
                session.setAttribute("Erreur", "Solde non ajusté");
            } 
            else
            {
               session.setAttribute("Erreur", "Votre capital a bien été modifié");
            }
            stm.close();            
        }
        catch(SQLException s){session.setAttribute("Erreur", s.getMessage()+ "\n");}
        finally{odc.deconnecter();}
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
