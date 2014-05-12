package EshoppeWeb;

import java.io.PrintWriter;
import javax.servlet.http.HttpSession;

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
    
    static protected void barreDeMenu(PrintWriter out, HttpSession session ){
            out.println("<table class='entete_catalogue'>");
                out.println("  <tr><td id='logo'><img src=\"logo.png\" alt=\"\" />"
                            + "</td>");
        String nomUser = (String)session.getAttribute("Nom_Joueur");
        if(nomUser != null)
            enteteJoueur(out, nomUser);
        else
            enteteVisiteur(out);
        
        out.println("</tr>");
        out.println("</table>");    
    }
    static protected void enteteJoueur(PrintWriter out, String nomUser){
        
        out.println("<td id='profil'><form action='profil' method='post'>"
                        + "<input type=\"submit\" value=\"Profil\" class=\"b_submit\" />"
                        + nomUser +"</form>"
                 + "</td>");////récupérer la valeur réelle pour le joueur
        
        //mettre form ici pour déconnecter (session.invalidate() et appel catalogue avec connecté == false
        out.println("<td id='deconnecter'><input type=\"submit\" value=\"Se déconnecter\" class=\"b_submit\" />"
                  + "</td>");
        
        /*<form action='panier' method='post'>*/
        out.println("\"<td id='panier'><input type=\"submit\" value=\"Panier\" class=\"b_submit\" />"
                  + "</td>");   
    }
    static protected void enteteVisiteur(PrintWriter out){
        
        /*<form action='login' method='post'>*/
        out.println("<td id='login'><form action='login' method='post'>"
                + "Alias: <input type=\"text\" name=\"user\" value=\"nom usager\" class='marge' />"
                + " Mot de passe: <input type=\"password\" class='marge' name=\"motdepasse\"/>"
                + "<input type=\"submit\" value=\"Login\" class=\"b_submit\"/>"
                + "</form></td>");
        /*<form action='inscription' method='post'>*/
        out.println("<td id='inscription'><form action='inscription' method='get'>"
                + "<input type=\"submit\" value=\"S'inscrire\" class=\"b_submit\" /></form></td>");
    }
    static protected void piedsDePage(PrintWriter out){
        out.println("</body>");
        out.println("</html>");
    }    
}
