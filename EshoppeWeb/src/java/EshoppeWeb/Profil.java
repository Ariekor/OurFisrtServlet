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
@WebServlet(name = "Profil", urlPatterns = {"/profil"})
public class Profil extends HttpServlet {
   
   private HttpSession session;
   private String nomUser = "";
   private String motDePasse = "";
   private String nom = "";
   private String prenom = "";
   private int capUser;
   

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
      
      try (PrintWriter out = response.getWriter()) {
         obtenirInfosProfil();
         generatePageProfil(out, "");
      }
   }
   
   protected void generatePageProfil(PrintWriter out, String Erreur)
           throws ServletException , IOException{
      
          UtilHtml.enteteHtml(out,"Profil");          
          UtilHtml.barreDeMenu(out, session);
          UtilHtml.afficherErreurPage(out, session);
          out.println("<form action='profil' method='post'>");
            out.println("<div id='mainProfilDiv'>");
                  out.print("<table id='zeProfil'>");
                        out.println("<tr class='rowHeigh'>");
                           out.println("<td class='zeTitre' colspan='4'>Profil </td>");
                        out.println("</tr>");

                        out.println("<tr>");
                            out.println("<td colspan='2' class='labelRow'>Nom Usager : </td>");
                            out.println("<td colspan='2' class='zeChampTexte'>" + nomUser + "</td>");

                        out.println("</tr>");

                        out.println("<form action='modifiermdp' method='post'>");
                           out.println("<tr>");
                               out.println("<td colspan='2' class='labelRow'>Mot de passe : </td>");
                               out.println("<td colspan='2' class='zeChampTexte'><input type=\"text\" name=\"MotDePasse\" value=\"" + motDePasse + "\"/> </td>");

                           out.println("</tr>");

                           out.println("<tr class='zeOtherRow'>");
                               out.println("<td colspan='4' class='b_modif'><input type=\"submit\" value=\"Modifier\" class=\"b_submit\"/> </td>");
                           out.println("</tr>");
                        out.println("</form>");

                        out.println("<tr>");
                            out.println("<td colspan='2' class='labelRow'>Nom : </td>");
                            out.println("<td colspan='2' class='zeChampTexte'>" + nom + "</td>");

                        out.println("</tr>");

                        out.println("<tr>");
                            out.println("<td colspan='2' class='labelRow'>Prénom : </td>");
                            out.println("<td colspan='2' class='zeChampTexte'>"+ prenom + " </td>");

                        out.println("</tr>");
                        
                        out.println("<form action='modifiercapital' method='post'>");
                           out.println("<tr>");
                               out.println("<td colspan='2' class='labelRow'>Capital : </td>");
                               out.println("<td colspan='2' class='zeChampTexte'><input type=\"text\" name=\"Capital\" value=\"" + capUser + "\"/> </td>");

                           out.println("</tr>");

                           out.println("<tr>");
                               out.println("<td colspan='4' class='b_modif'><input type=\"submit\" value=\"Modifier\" class=\"b_submit\"/> </td>");
                           out.println("</tr>");
                        out.println("</form>");

                    out.println("</table>");
                out.println("</div>");
            out.println("</form>");
           
            UtilHtml.piedsDePage(out, session);
   }
   
    protected void obtenirInfosProfil()
    {
        String sqlProfil = "select MOTDEPASSE, NOM, PRENOM, CAPITAL from joueursrpg where nomusager = '"+ nomUser + "'";
        ConnectionOracle oradb = new ConnectionOracle();
        oradb.setConnection("kellylea", "oracle2");
        oradb.connecter(); 
        try
        {    
            Statement stm = oradb.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rst = stm.executeQuery(sqlProfil);
            if(rst.first())
            {
                motDePasse = rst.getString( "MOTDEPASSE" );
                nom = rst.getString("NOM");
                prenom = rst.getString("PRENOM");
                capUser = (Integer)rst.getInt("CAPITAL");
            }
            rst.close();
            stm.close();            
        }
        catch (SQLException e){/*faire quelquechose ici*/} 
        finally{oradb.deconnecter();}
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
              response.setContentType("text/html;charset=UTF-8");
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

}
