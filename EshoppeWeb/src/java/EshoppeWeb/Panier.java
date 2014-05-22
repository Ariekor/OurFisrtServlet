package EshoppeWeb;

/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

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
 * @author Isabelle
 */
@WebServlet(urlPatterns = {"/panier"})
public class Panier extends HttpServlet {
    
    private HttpSession session;
    //à récupérer du cookie
    private String erreur = "";    
    private String nomUser;
    private int capUser = 100;
    private int total = 0;//total du panier

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
        erreur = (String)session.getAttribute("Erreur");
        loadCapItem();
        try (PrintWriter out = response.getWriter()) {
            
            UtilHtml.enteteHtml(out,"Panier");
            UtilHtml.barreDeMenu(out, session);
            
            // début de code pour inventaire
            out.println("<h1>Panier</h1>");
            UtilHtml.afficherErreurPage(out, session);
            out.println("<div class='zeCatalogueDiv'>");
            listeItems(out);// bouton retirer item inclus
            //fin de code pour inventaire
            
            menuPanier(out); //colonne de droite, menu
            out.println("</div>"); ///////inclure pour inventaire
            UtilHtml.piedsDePage(out, session);
        }
        session.setAttribute("Erreur", erreur);
    }
    
    private void loadCapItem()
    {
        ConnectionOracle connBd = new ConnectionOracle();
        connBd.setConnection("kellylea", "oracle2");
        connBd.connecter();
        try
        {
            CallableStatement stm = connBd.getConnection().prepareCall("{ ? = call Gestion_Users.listerCapital(?) }");
            stm.registerOutParameter(1, OracleTypes.CURSOR);
            stm.setString(2, nomUser);
            stm.execute();
            ResultSet rst = (ResultSet)stm.getObject(1); 
            
            if (rst.next())
            {
                capUser = rst.getInt(1);
            } 
            rst.close();
            stm.close();              
        }
        catch(SQLException e){erreur = e.getMessage();}
        finally{connBd.deconnecter();}
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
        //session.setAttribute("Erreur", "");
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
    
    private void listeItems(PrintWriter out){
        
        ///////est-ce qu'on devrait faire un genre de struct et mettre tous dans un array ou vecteur??????
        String numitem;
        String nomitem;
        int qte;
        int prixUnitaire;
        int prixCalcule ; 
        total = 0;
        
        out.println( "<div id='listePanier'><table class='zeCatable'>" );
        //entête panier
        out.println( "<tr><td class='zeCatEntete'>Nom d'item</td>"
                + "<td class='zeCatEntete'>Qté au panier</td>"
                + "<td class='zeCatEntete'>Prix unitaire</td>"
                + "<td class='zeCatEntete'>Prix calculé</td>"
                + "<td class='zeCatEntete'>Retirer du panier</td>");
        
        // connexion à la base de données
        ConnectionOracle oradb = new ConnectionOracle();
        oradb.setConnection("kellylea", "oracle2");
        oradb.connecter();
        try
        {            
            ResultSet panier;
            CallableStatement stmP = oradb.getConnection().prepareCall("{ ? = call Gestion_Panier.lister(?)}" );
            stmP.registerOutParameter(1, OracleTypes.CURSOR);
            stmP.setString( 2, nomUser );
            stmP.execute();
            panier = (ResultSet)stmP.getObject(1);
            
            // parcours du ResultSet
            while( panier.next() )
            {
                //récupérer les valeurs de chaque ligne
                out.println( "<tr class='zeCatalogueRow'>" );
                numitem = ((Integer)panier.getInt("NUMITEM")).toString();
                nomitem = panier.getString( "NOMITEM" );
                qte = (Integer)panier.getInt("QUANTITEITEM");
                prixUnitaire = (Integer)panier.getInt("PRIX");
                prixCalcule =  ((Integer)panier.getInt("QUANTITEITEM"))*((Integer)panier.getInt("PRIX"));
                total += prixCalcule;
                //chaque ligne est un form qui permet de retirer un objet du panier
                out.println("<form action='modifierqtepanier' method='post'>"
                        + "<input type=\"hidden\" name=\"numitem\" value=\""
                        + numitem + "\"/><td class='zeCatalogueCell'>"
                        + nomitem + "</td><td class='zeCatalogueCell'>"                        
                        + "<input type=\"text\" name='qte' value =\""+qte+"\" size='3' onkeyup=\"ConstrainToDigit(event);\">"
                        + "<input type=\"submit\" class=\"b_submit\" value=\"Mettre à jour\" name=\"update\" />"//bouton mettre a jour
                        + "</form></td><td class='zeCatalogueCell'>"
                        
                        + prixUnitaire + "</td><td class='zeCatalogueCell'>"
                        + prixCalcule + "</td><td class='zeCatalogueCell'>"
                        
                        + "<form action='retirerpanier' method='post'><input type=\"hidden\" name=\"numitem\" value=\""
                        + numitem + "\"/>"
                        + "<input type=\"submit\" value='X' class=\"b_submit\"/></form></td>" );////doit appeler supprimer item/panier
         
                out.println( "</tr>" );                
            }
            panier.close(); 
            stmP.close();            
        }
        catch( SQLException se )
        {
            out.println( se.getMessage() + "  Panier vide."  );
        }
        finally {oradb.deconnecter();}
        out.println( "</table></div>" );
    }
    
    private void menuPanier(PrintWriter out)
    {
        out.println("<div id='menuPanier'><table class='zeCatable'>");
        out.println("<tr><td>Votre capital:</td></tr>"
                + "<tr><td><input type='hidden' name='cap' value='"+capUser+"'>"+capUser+"</td></tr>"
                + "<tr><td></td></tr>"
                + "<tr><td></td></tr>"
                + "<tr><td></td></tr>"
                + "<tr><td></td></tr>"
                + "<tr><td>Total panier:</td></tr>"
                + "<tr><td><input type='hidden' name='total' value='"+total+"'>"+total+"</td></tr>"
                + "<tr><td></td></tr>"
                + "<tr><td></td></tr>"
                
                //bouton pour acheter le contenu entier du panier 
                + "<tr><td><form action='acheterpanier' method='post'>"
                + "<input type='hidden' name='cap' value='"+capUser+"'>"
                + "<input type='hidden' name='total' value='"+total+"'>"
                + "<input type=\"submit\" class=\"b_submit\" value=\"Acheter\" name=\"achat\" />"
                + "</form></td></tr>"
                
                //bouton pour vider le panier et retourner au catalogue
                + "<tr><td><form action='viderpanier' method='post'>"
                + "<input type=\"submit\" class=\"b_submit\" value=\"Vider\" name=\"vider\" />"
                + "</form></td></tr>"
                
                //bouton pour fermer le panier et retourner au catalogue
                + "<tr><td><form action='catalogue' method='get'>"
                + "<input type=\"submit\" class=\"b_submit\" value=\"Fermer\" name=\"fermer\" />"
                + "</form></td></tr>");
        
        out.println("</table>"
                + "</div>");
    }
}
