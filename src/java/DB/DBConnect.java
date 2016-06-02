/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import com.mongodb.MongoClient;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.bson.Document;

/**
 *
 * @author delta
 */
public class DBConnect {
    
    static void Connect(HttpServletRequest request,PrintWriter out,String operation,String DBName,String CollName){
        MongoClient mongoClient = new MongoClient("10.42.0.1",27017);
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
            AggregateIterable<Document> agg = coll.aggregate(list);
                
            PrintTable(out,agg);
                
            
        } else if(operation.equalsIgnoreCase("set")){
                
            coll.insertOne(Document.parse(json));
                
        } else if(operation.equalsIgnoreCase("remove")){
                
            coll.deleteMany(Document.parse(json));
                
                
        } else if(operation.equalsIgnoreCase("update")){
                
            String json2 = request.getParameter("q2");
            coll.updateMany(Document.parse(json),Document.parse(json2));
                    
        }
    }
    
    static void PrintTable(PrintWriter out,AggregateIterable<Document> agg){
        out.println("<table BORDER=1 CELLPADDING=0 CELLSPACING=0 WIDTH=50% >");
                
        Set<String> setkey = agg.iterator().next().keySet();
                
        out.println("<tr>");
        for(String key : setkey) out.print("<td>"+key+ "</td>");
        out.println("</tr>");
                
        for(Document doc : agg){
            out.println("<tr>");
            for(String key : setkey) out.print("<td>"+doc.get(key)+ "</td>");
            out.println("</tr>");
        }
                
        out.println("</table>");
    }
    
}
