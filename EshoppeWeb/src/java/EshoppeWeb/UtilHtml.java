package EshoppeWeb;

import java.io.PrintWriter;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Isabelle
 */
public class UtilHtml {
    //méthodes statiques
    
    
    static public void enteteHtml(PrintWriter out, String page){    
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>É-Shop-pe : "+page+"</title>"); 
        out.println("<meta charset=\"UTF-8\">");
        out.println("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");   
        out.println("<link rel=\"stylesheet\" href=\"Eshoppe.css\"  type=\"text/css\"/>");            
        out.println("</head>");
        out.println("<body>");
        out.println("<a href=\"http://localhost:8080/eshoppeweb/catalogue\" "
                + "target=\"_parent\"><h1>image ici</h1></a>");//////////a changer
    }
    static protected void barreDeMenu(PrintWriter out, boolean connecté ){
        out.println("<div class='entete_catalogue'>");
        out.println("  <div id='logo'><a href=\"http://localhost:8080/eshoppeweb/catalogue\" "
                             + "target=\"_parent\">LOGO</a>"
                    + "</div>");
        if(connecté)
            enteteJoueur(out);
        else
            enteteVisiteur(out);
        out.println("</div>");    
    }
    static protected void enteteJoueur(PrintWriter out){
        
        out.println("<div id='profil'><form action='profil' method='post'>"
                        + "<input type=\"submit\" value=\"Profil\" class=\"b_submit\" />"
                        + "  Joueur connecté </form>"
                 + "</div>");////récupérer la valeur réelle pour le joueur
        
        //mettre form ici pour déconnecter (session.invalidate() et appel catalogue avec connecté == false
        out.println("<div id='deconnecter'><input type=\"submit\" value=\"Se déconnecter\" class=\"b_submit\" />"
                  + "</div>");
        
        /*<form action='panier' method='post'>*/
        out.println("\"<div id='panier'><input type=\"submit\" value=\"Panier\" class=\"b_submit\" />"
                  + "</div>");   
    }
    static protected void enteteVisiteur(PrintWriter out){
        
        /*<form action='login' method='post'>*/
        out.println("<div id='login'><input type=\"submit\" value=\"Login\" class=\"b_submit\"/>"
                  + " Alias: <input type=\"text\" name=\"user\" value=\"nom usager\"/>"
                  + " Mot de passe: <input type=\"password\" name=\"motdepasse\"/></div>");
        /*<form action='inscription' method='post'>*/
        out.println("<div id='inscription'><input type=\"submit\" value=\"S'inscrire\" class=\"b_submit\" /></div>");
    }
    static protected void piedsDePage(PrintWriter out){
        out.println("</body>");
        out.println("</html>");
    }    
}
