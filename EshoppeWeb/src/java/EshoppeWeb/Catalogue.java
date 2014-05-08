/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package EshoppeWeb;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import oracle.jdbc.OracleTypes;

/**
 *
 * @author Isabelle
 */
@WebServlet(name = "Catalogue", urlPatterns = {"/catalogue"})
public class Catalogue extends HttpServlet {

    /////a récupérer de session Tomcat...
    boolean connecté = false;
    
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String user = request.getParameter("user");
        String mdpasse = request.getParameter("motdepasse");
        String genre = request.getParameter("genre");
        String nomcle = request.getParameter("motcle");
        
        response.setContentType("text/html;charset=UTF-8");  
        PrintWriter out = response.getWriter();
        try {    
            
            UtilHtml.enteteHtml(out);
            
            
            out.println("<h1>The super duper Catalogue!!! </h1>");
            UtilHtml.barreDeMenu(out, connecté);
            menuRecherche(out);            
            
            listeItems(out, "Tout le catalogue");//////////////////
            
            barreNavigation(out);
    
            UtilHtml.piedsDePage(out);
        }
      //  catch()
        finally
        {
           out.close();
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
            
        processRequest(request,response);
        
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
        
        
        processRequest(request,response);
        
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

      
    private void menuRecherche(PrintWriter out, String g){
        
        out.println("<div class='entete_catalogue'>"
                        + "<form action='catalogue' method='post'>"
                        + "     Rechercher dans: <select name=\"genre\" /> ");////remplir par méthode
        
        out.println(      "         <option selected>Tout le catalogue</option>" 
                        + "         <option>Arme</option>"
                        + "         <option>Armure</option>"                 
                        + "         <option>Habileté</option>" 
                        + "         <option>Potion</option>" 
                        + "     </select> "
                        + "     Mot clé: <input type=\"text\" name=\"nomCle\" />"
                        + "              <input type=\"submit\" value=\"Afficher\" class=\"b_submit\" />"
                        + "</form>"
                   + "</div>");
    }
    
    private void listeItems(PrintWriter out, String genre ){
        
        String nomitem;
        String qte ;
        String prix;
        String poids ;
        String genreItem ;
          
            // connexion à la base de données
      ConnectionOracle oradb = new ConnectionOracle();
      oradb.setConnection("kellylea", "oracle2");
      oradb.connecter();  
      out.println( "<div><table>" );
         //entête
         out.println( "<tr><td>Nom d'item</td><td>Quantité</td><td>" 
                    + "Prix</td><td>Poids</td><td>Genre</td></tr></br></br>" );

      try
      {
          ResultSet rst;
            try (CallableStatement stm = oradb.getConnection().prepareCall("{ ? = call Gestion_Catalogue.listercatalogue(?)}", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY )) {
                stm.registerOutParameter(1, OracleTypes.CURSOR);
                stm.setString( 2, genre );
                stm.execute();
                rst = (ResultSet)stm.getObject(1);
                // parcours du ResultSet
                while( rst.next() )
                {
                    /*NOMITEM, QUANTITE, PRIX, POIDS, GENRE*/
                    out.println( "<tr>" );
                    nomitem = rst.getString( "NOMITEM" );
                    qte = ((Integer)rst.getInt("QUANTITE")).toString();
                    prix = ((Integer)rst.getInt("PRIX")).toString();
                    poids = ((Integer)rst.getInt("POIDS")).toString();
                    genreItem = rst.getString( "GENRE" );
                    
                    out.println( "<td>" + nomitem + "</td><td>" +  qte + "</td><td>"
                            +  prix + "</td><td>" +  poids + "</td><td>" +  genreItem + "</td>" );
                    out.println( "</tr>" );
                }  }
         rst.close();    
      }
      catch( SQLException se ) 
      {
         out.println( se.getMessage() );
      }
      out.println( "</table></div>" );
      oradb.deconnecter(); 
    }
    
    
    private void barreNavigation(PrintWriter out){
            //ajouter barre de navigation href vers debut, précédent, suivent et fin
            // exemple de barre de navigation
      //   out.println( "<a href='insertion'>Insertion</a>" + " | " +
      //         "<a href='recherche'>Recherche</a>" );
          
    }
}
    
