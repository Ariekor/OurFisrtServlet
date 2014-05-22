/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package EshoppeWeb;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import oracle.jdbc.OracleTypes;

/**
 *
 * @author Léa
 */
@WebServlet(name = "InventaireJoueur", urlPatterns = {"/inventairejoueur"})
public class InventaireJoueur extends HttpServlet {

   private HttpSession session;
   private String nomUser ="";
   private String erreur ="";
   
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
          UtilHtml.enteteHtml(out,"InventaireJoueur");          
          UtilHtml.barreDeMenu(out, session);
          UtilHtml.gererErreurs(session, erreur);
          
          out.println("<h1>Inventaire du joueur</h1>");
          listeItems(out);
          out.println("<form action='catalogue' method='get'>"
                  + "<input type=\"submit\" value=\"Fermer\" class=\"b_submit\" name='fermerinventaire' />"
                  + "</form>");
          
          UtilHtml.piedsDePage(out, session);
      }
   }
   
   private void listeItems(PrintWriter out){
        
        ///////est-ce qu'on devrait faire un genre de struct et mettre tous dans un array ou vecteur??????
        String nomitem="";
        int numitem;
        int qte;
        
        out.println( "<div id='listeItemsJoueur'><table class='zeCatable'>" );
        //entête panier
        out.println( "<tr><td class='zeCatEntete'>Nom d'item</td>"
                + "<td class='zeCatEntete'>Quantité</td>"
                + "<td class='zeCatEntete'>Genre</td>");
        
        // connexion à la base de données
        ConnectionOracle oradb = new ConnectionOracle();
        oradb.setConnection("kellylea", "oracle2");
        oradb.connecter();
        try
        {            
            ResultSet inventaireJoueur;
            CallableStatement stmP = oradb.getConnection().prepareCall("{ ? = call Gestion_InventaireJoueur.lister(?)}" );
            stmP.registerOutParameter(1, OracleTypes.CURSOR);
            stmP.setString( 2, nomUser );
            stmP.execute();
            inventaireJoueur = (ResultSet)stmP.getObject(1);
            
            // parcours du ResultSet
            while( inventaireJoueur.next() )
            {
                //récupérer les valeurs de chaque ligne
                out.println( "<tr class='zeCatalogueRow'>" );
                nomitem = inventaireJoueur.getString( "NOMITEM" );
                qte = (Integer)inventaireJoueur.getInt("QUANTITEITEM");
                numitem = (Integer)inventaireJoueur.getInt("NUMITEM");
                //chaque ligne est un form qui permet de retirer un objet du panier
                out.println("<td class='zeCatalogueCell'>"
                        + nomitem
                        + "</td>"
                        + "<td class='zeCatalogueCell'>"                        
                        + qte
                        + "</td>"
                        + "<td class='zeCatalogueCell'>"
                        + chercherGenre(numitem)
                        + "</td>");
         
                out.println( "</tr>" );                
            }
            inventaireJoueur.close(); 
            stmP.close();            
        }
        catch( SQLException se )
        {
            out.println( se.getMessage() + " Inventaire vide"  );
        }
        finally {oradb.deconnecter();}
        out.println( "</table></div>" );
    }
   
   private String chercherGenre(int numitem)
   {
        String genre="";
        String SQLGenre = "Select Genre From Catalogue Where NUMITEM = " + numitem;
        
        ConnectionOracle oradbListe = new ConnectionOracle();
        oradbListe.setConnection("kellylea", "oracle2");
        
        oradbListe.connecter();  
        try
        {  
            Statement stm = oradbListe.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rst = stm.executeQuery(SQLGenre); 
                        
            while(rst.next())
            {
                genre = rst.getString("genre");
            }
            
            rst.close();
            stm.close();
        }
        catch (SQLException e){/*faire quelquechose ici*/}   
        finally{ oradbListe.deconnecter();}
        return genre;
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

}
