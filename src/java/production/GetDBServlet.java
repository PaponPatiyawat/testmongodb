/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package production;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.bson.Document;

//@WebServlet(name = "GetDBServlet", urlPatterns = "/get/*")
public class GetDBServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param url
     */
    protected Document CreateDocument(String url){
        String field = url.substring(0,url.indexOf("="));
        String value = url.substring(url.indexOf("=")+1,url.length());
        return new Document(field, value);
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet GetDBServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet GetDBServlet at " + request.getContextPath() + "</h1>");
            
            
            String path = request.getPathInfo();
            path = path.substring(1,path.length());
            
            
            String DBName = path.substring(0,path.indexOf("/"));
            String CollName = path.substring(path.indexOf("/")+1,path.length());
            
            
            MongoClient mongoClient = new MongoClient();
            MongoDatabase db = mongoClient.getDatabase(DBName);
            MongoCollection<Document> coll = db.getCollection(CollName);

            
            String json = request.getParameter("q");
            
            
            try (MongoCursor<Document> cursor = coll.find(Document.parse(json)).iterator()) {
                while(cursor.hasNext()) {
                    out.println(cursor.next().toJson() + "<BR>");
                    out.println("<BR>");
                }
            }
            
            
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
