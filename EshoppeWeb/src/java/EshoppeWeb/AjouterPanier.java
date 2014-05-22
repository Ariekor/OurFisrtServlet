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
import javax.websocket.Session;

/**
 *
 * @author Simon
 */
@WebServlet(name = "AjouterPanier", urlPatterns = {"/ajouterpanier"})
public class AjouterPanier extends HttpServlet {

    HttpSession session ;
    String erreur;
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
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet AjouterPanier</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AjouterPanier at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
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
        String retourUrl = "http://localhost:8080/eshoppeweb/panier";
        session = request.getSession();
        String nomUser = (String)session.getAttribute("Nom_Joueur");
        
        if ( nomUser == null)//session ne sera jamais null, seul le user a vérifier
        {
            session.setAttribute("Erreur", "Vous devez être connecté pour ajouter au panier");
            retourUrl = "http://localhost:8080/eshoppeweb/catalogue";
        }
        else
        {
            try
            {
                int numItem = Integer.parseInt(request.getParameter("numitem"));
                int stock = Integer.parseInt(request.getParameter("stock")); 
                int quantite = Integer.parseInt(request.getParameter("qte"));
                String nomItem = request.getParameter("nomItem");
                               
                if(quantite > stock)
                {
                    session.setAttribute("Erreur", "Stock maximum disponible pour: "+nomItem+" = "+ stock );
                    retourUrl = "http://localhost:8080/eshoppeweb/catalogue";
                }
                else
                {
                    //ajouter au panier
                    String resultat = ajouterPanier(nomUser,numItem,quantite);
                    if (!resultat.equals(""))
                    {
                        session.setAttribute("Erreur", resultat + "\n");
                        retourUrl = "http://localhost:8080/eshoppeweb/catalogue";
                    }
                    else
                    {
                        retourUrl = "http://localhost:8080/eshoppeweb/panier";
                    }
                }
            }
            catch(NumberFormatException E){
                    session.setAttribute("Erreur", "Il faut entrer une quantité au bouton ajouter correspondant");
                    retourUrl = "http://localhost:8080/eshoppeweb/catalogue";
            }
        }      
        
        response.sendRedirect(retourUrl);
        
        
    }
    
    private String ajouterPanier(String nomUser, int numItem, int quantite)
    {
        String err = "";
        if(estDejaDansPanier(nomUser,numItem))
        {
            modifierQte(nomUser,numItem,quantite);
        }
        else
        {
            ConnectionOracle odc = new ConnectionOracle();
            odc.setConnection("kellylea", "oracle2");
            odc.connecter();
            
            try{
                CallableStatement stm = odc.getConnection().prepareCall("{call GESTION_PANIER.INSERTION( ? , ? , ? )}");
                stm.setString(1, nomUser);
                stm.setInt(2, numItem);
                stm.setInt(3, quantite);
                int ajout = stm.executeUpdate();
                if (ajout == 0)
                {
                    err += "\n L'item n'est pas ajouté...\n ";
                }
                stm.close();                
            }
            catch(SQLException sqe){session.setAttribute("Erreur", sqe.getMessage()+ "\n");}
            finally{odc.deconnecter();}
        }        
        return err;
    }
    private boolean estDejaDansPanier(String nom , int numItem)
    {
        boolean valide = false;
        String sqlItem = "select nomusager from panier where nomusager = '"+nom+"' and numitem = '"+numItem+"'";
        ConnectionOracle oradb = new ConnectionOracle();
        oradb.setConnection("kellylea", "oracle2");
        oradb.connecter(); 
        try
        {        
             
            Statement stm = oradb.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rst = stm.executeQuery(sqlItem);
            if(rst.first())
            {
                valide = true;
            }
            rst.close();
            stm.close();            
        }
        catch (SQLException e){session.setAttribute("Erreur", e.getMessage()+ "\n");} 
        finally {oradb.deconnecter();}
        return valide;
    }
    
    
    
    private int obtenirQtePanier (String nom , int numItem)
    {
        int qte = 0;
        String Sql= "Select QUANTITEITEM FROM panier where nomusager='"+nom+"' and numitem='"+numItem+"'";
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
        catch(SQLException s){session.setAttribute("Erreur", s.getMessage() + "\n");}
        finally{oradbPanier.deconnecter();}
        return qte;
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
            stm.setInt(3, quantite + obtenirQtePanier(nom,numItem) );
            int modifie = stm.executeUpdate();
            if (modifie == 0)
            {
                session.setAttribute("Erreur", "Quantité non ajustée.");
            }            
            stm.close();            
        }
        catch(SQLException s){session.setAttribute("Erreur", s.getMessage()+ "\n");}
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
