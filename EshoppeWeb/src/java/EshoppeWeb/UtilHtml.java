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
        out.println("<div class='entete_generale'>");
        out.println("   <form>");
        out.println("       <tr><td><a href=\"http://localhost:8080/eshoppeweb/catalogue\" "
                + "target=\"_parent\">LOGO</a></td>");
        if(connecté)
            enteteJoueur(out);
        else
            enteteVisiteur(out);
        out.println("       </tr> ");
        out.println("   </form>");
        out.println("</div>");    
    }
    static protected void enteteJoueur(PrintWriter out){
        
        out.println("<td><form action='profil' method='post'><input type=\"submit\" value=\"Profil\" class=\"b_submit\" /></form>"
                  + "Alias du joueur connecté</td>");////récupérer la valeur réelle
        //mettre form ici pour déconnecter (session.invalidate() et appel catalogue avec connecté == false
        out.println("<td><input type=\"submit\" value=\"Se déconnecter\" class=\"b_submit\" /></td>");
        
        /*<form action='inventaire' method='post'>*/
        out.println("<td><input type=\"submit\" value=\"Inventaire\" class=\"b_submit\" /></td>");
        
        /*<form action='panier' method='post'>*/
        out.println("<td><input type=\"submit\" value=\"Panier\" class=\"b_submit\" /></td>");   
    }
    static protected void enteteVisiteur(PrintWriter out){
        
        /*<form action='login' method='post'>*/
        out.println("<td><input type=\"submit\" value=\"Login\" class=\"b_submit\"/>"
                  + " Alias: <input type=\"text\" name=\"user\" value=\"nom usager\"/>"
                  + " Mot de passe: <input type=\"password\" name=\"motdepasse\"/></td>");
        /*<form action='inscription' method='post'>*/
        out.println("<td><input type=\"submit\" value=\"S'inscrire\" class=\"b_submit\" /></td>");
    }
    static protected void piedsDePage(PrintWriter out){
        out.println("</body>");
        out.println("</html>");
    }    
}
