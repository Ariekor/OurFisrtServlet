/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package EshoppeWeb;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Isabelle
 */
@WebServlet(name = "Catalogue", urlPatterns = {"/catalogue"})
public class Catalogue extends HttpServlet {

    /////a récupérer de session Tomcat...
    boolean connecté = true;
    
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
        PrintWriter out = response.getWriter();
        try {    
            
            UtilHtml.enteteHtml(out);
            
            out.println("<body>");
            out.println("<h1>The super duper Catalogue!!! </h1>");
            UtilHtml.barreDeMenu(out, connecté);
            enteteCatalogue(out);            
            
            listeItems(out);
            
            barreNavigation(out);
    
            UtilHtml.piedsDePage(out);
        }
        finally
        {
           out.close();
        } 
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
        String user = request.getParameter("user");
        String mdpasse = request.getParameter("motdepasse");
        String genre = request.getParameter("genre");
        String nomcle = request.getParameter("motcle");
        
        response.setContentType("text/html;charset=UTF-8");  
        PrintWriter out = response.getWriter();
        try {    
            
            UtilHtml.enteteHtml(out);
            ConnectionOracle oradb = new ConnectionOracle();
            oradb.connecter();
            
            String sql = "ecrire sql si pas de methode ou fonctio package";
            try
            {
                //
                PreparedStatement stmins =
                      oradb.getConnection().prepareStatement( sql );

                // on affecte les valeurs aux paramètres de la requête
                stmins.setString( 1, user );
                stmins.setString( 2, mdpasse );
                stmins.executeUpdate();
                //oradb.getConnexion().commit();
                stmins.close();
             }
             catch( SQLException se ) 
             {
                System.err.println( se.getMessage() );
             }    

         // confirmation de l'insertion
   //      out.println( "<p>Vous avez ajouté l'employé suivant:</p>" );
   //      out.println( "<p>" + prenom + " " + nom + " (" + dep + ")</p>" );         
      }
      finally
      {
         out.close();
      }    
        
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

      
    private void enteteCatalogue(PrintWriter out){
                
        out.println("<form ><table>"
                + "         <tr>"
                + "             <td>Rechercher dans: <select name=\"genre\" /> "////remplir par méthode
                + "                     <option selected>Tout le catalogue</option>" 
                + "                     <option>Armes</option>"
                + "                     <option>Armures</option>"                 
                + "                     <option>Habiletés</option>" 
                + "                     <option>Potions</option>" 
                + "             </select> "
                + "             </td>"
                + "             <td>Mot clé: <input type=\"text\" id=\"nomCle\" />"
                + "                 <input type=\"submit\" value=\"Afficher\"/></td>"
                + "         </tr>"
                + "</table></form>");
    }
    private void listeItems(PrintWriter out){
        //tableau affichage mettre dans une méthode genererListeItems() prend 0,1 ou 2 params.
            //doit afficher 10 ou 20 items à la fois
            out.println("     <table>"
                    + "         <!--Le tableau qui contient la liste-- "
                    + "         <tr> for each colonne: <td>titre colonne</td></tr>"
                    + "           for each item"
                    + "         <tr> for each colonne <td>titre colonne</td></tr>"
                    + "     </table>"
                    + "</div>");
            
            
            
            
            
            // connexion à la base de données
      ConnectionOracle oradb = new ConnectionOracle();
      oradb.connecter();
      
      //
      String sql= "select nom, prenom from employes e inner join " +
            "departements d  on e.codedep = d.codedep where d.nomdep = ?";

      try
      {
         // passer la requête par le PreparedStatement
         PreparedStatement stm = oradb.getConnection().prepareStatement( sql );
         // affecter la valeur "informatique" au paramètre de la requête sql
         // le paramètre est représenté par le ?
         stm.setString( 1, "informatique" );
         ResultSet rst = stm.executeQuery();

         // parcours du ResultSet
         out.println( "<ol>" );
         while( rst.next() )
         {
            String nom = rst.getString( "nom" );
            String prenom = rst.getString( "prenom" );
            out.println( "<li>" + nom + ", " +  prenom + "</li>" );
         }
         out.println( "</ol>" );
                  
         stm.close();
         rst.close();    
      }
      catch( SQLException se ) 
      {
         System.err.println( se );
      }    

      oradb.deconnecter();
            
            
            
            
            
    }
    private void barreNavigation(PrintWriter out){
            //ajouter barre de navigation href vers debut, précédent, suivent et fin
            // exemple de barre de navigation
      //   out.println( "<a href='insertion'>Insertion</a>" + " | " +
      //         "<a href='recherche'>Recherche</a>" );
          
    }
}
    
