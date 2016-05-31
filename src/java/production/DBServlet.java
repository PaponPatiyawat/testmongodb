/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package production;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.bson.Document;

@WebServlet(name = "DBServlet", urlPatterns = "/*")
public class DBServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     */
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet DBServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet DBServlet at " + request.getContextPath() + "</h1>");
            
            
            String path = request.getPathInfo();
            path = path.substring(1,path.length());
            
            
            String operation = path.substring(0,path.indexOf("/"));
            String DBName = path.substring(path.indexOf("/")+1,path.lastIndexOf("/"));
            String CollName = path.substring(path.lastIndexOf("/")+1,path.length());

            
            MongoClient mongoClient = new MongoClient();
            MongoDatabase db = mongoClient.getDatabase(DBName);
            MongoCollection<Document> coll = db.getCollection(CollName);
            
            String json = request.getParameter("q");
            
            if(operation.equalsIgnoreCase("get")){
                
                List<Document> list = new ArrayList<>();
                
                Map<String,String[]> map = request.getParameterMap();
                for (Map.Entry entry : map.entrySet()) {
                    String[]  values = (String[])entry.getValue();
                    list.add(Document.parse(values[0]));
                }
                
                for(Document doc : coll.aggregate(list)){
                    out.println(doc.toJson() + "<BR>" + "<BR>");
                }
            
            } else if(operation.equalsIgnoreCase("set")){
                
                coll.insertOne(Document.parse(json));
                
            } else if(operation.equalsIgnoreCase("remove")){
                
                coll.deleteMany(Document.parse(json));
                
            } else if(operation.equalsIgnoreCase("update")){
                
                String json2 = request.getParameter("q2");
                coll.updateMany(Document.parse(json),Document.parse(json2));
                    
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
