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

/**
 *
 * @author Isabelle
 */
@WebServlet(name = "ModifierQtePanier", urlPatterns = {"/modifierqtepanier"})
public class ModifierQtePanier extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
   HttpSession session;
   String nomUser;
   int numItem;
   int nouvelleQte;
   String erreur = "";

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
        response.sendRedirect("barreNavigation(out);    \n" +
"            UtilHtml.piedsDePage(out, session);catalogue");
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
       nomUser= (String)session.getAttribute("Nom_Joueur");
       numItem = Integer.parseInt(request.getParameter("numitem"));
       nouvelleQte = Integer.parseInt(request.getParameter("qte"));
       ajusterQte(); 
       if (erreur != null && !erreur.equals("null"))
        {
            session.setAttribute("Erreur", erreur);
        }
        else 
        {
            session.setAttribute("Erreur", "");
        }
       response.sendRedirect("http://localhost:8080/eshoppeweb/panier");               
    }
    private void ajusterQte()
    {
        //Aller chercher la qte en stock
        int stock = obtenirQteCatalogue(numItem);
        // Si qteStock > qte
        if ( stock >= nouvelleQte)
        {
            modifierQte(nomUser , numItem , nouvelleQte);
        } 
        //sinon            
        else
        {
            // Envoyer un message d'erreur
            erreur += "Stock maximum disponible = "+ stock ;
        }        
    }    
    
    private void modifierQte(String nom , int numItem , int quantite)
    {
        ConnectionOracle odc = new ConnectionOracle();
        odc.setConnection("kellylea", "oracle2");
        odc.connecter();
        try{
            CallableStatement stm = odc.getConnection().prepareCall("{call GESTION_PANIER.MODIFIERQUANTITE( ? , ? , ? )}");
            stm.setInt(1, numItem);
            stm.setString(2,nom);
            stm.setInt(3, quantite );
            int modifie = stm.executeUpdate();
            if (modifie == 0)
            {
                erreur+= "Quantité non ajustée.";
            }            
            stm.close();            
        }
        catch(SQLException s){erreur += s.getMessage()+ "\n";}
        finally{odc.deconnecter();}
    }
    
    private int obtenirQteCatalogue (int numItem)
    {
        int qte = 0;
        String Sql= "Select QUANTITE FROM catalogue where numitem='"+numItem+"'";
        ConnectionOracle oradbPanier = new ConnectionOracle();
        oradbPanier.setConnection("kellylea", "oracle2");
        oradbPanier.connecter();
        try
        {
            Statement stm = oradbPanier.getConnection().createStatement();
            ResultSet rst = stm.executeQuery(Sql);
            if (rst.next())
            {
                qte = rst.getInt(1);
            }
            rst.close();
            stm.close();
        }
        catch(SQLException s){erreur += s.getMessage() + "\n";}
        finally{oradbPanier.deconnecter();}
        return qte;
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
