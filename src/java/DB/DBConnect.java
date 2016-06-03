/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import com.mongodb.MongoClient;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
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
        
        setDB(request,coll);
                       
        List<Document> list = new ArrayList<>();
                
        Map<String,String[]> map = request.getParameterMap();
        for (Map.Entry entry : map.entrySet()) {
            String[]  values = (String[])entry.getValue();
            list.add(Document.parse(values[0]));
        }
        AggregateIterable<Document> agg = coll.aggregate(list);
                
        PrintTable(out,agg);
        //PrintJson(out,agg);

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
    
    
    
    static void PrintTable(PrintWriter out,FindIterable<Document> find){
        out.println("<table BORDER=1 CELLPADDING=0 CELLSPACING=0 WIDTH=50% >");
                
        Set<String> setkey = find.iterator().next().keySet();
                
        out.println("<tr>");
        for(String key : setkey) out.print("<td>"+key+ "</td>");
        out.println("</tr>");
                
        for(Document doc : find){
            out.println("<tr>");
            for(String key : setkey) out.print("<td>"+doc.get(key)+ "</td>");
            out.println("</tr>");
        }
                
        out.println("</table>");
    }
    
    static void setDB(HttpServletRequest request,MongoCollection<Document> coll){
        String AddString = request.getParameter("add");
        if(AddString != null) coll.insertOne(Document.parse(AddString));
        
        String RemoveString = request.getParameter("remove");
        if(RemoveString != null) coll.deleteMany(Document.parse(RemoveString));
        
        String Update1String = request.getParameter("update1");
        String Update2String = request.getParameter("update2");
        if(Update1String != null && Update2String != null) coll.updateMany(Document.parse(Update1String),Document.parse(Update2String));
    }
    
    
    static void PrintJson(PrintWriter out,AggregateIterable<Document> agg){
                
        Set<String> setkey = agg.iterator().next().keySet();
        
                
        for(Document doc : agg){
            
            out.println(doc.toJson()+"<br/>");
        }

    }
}
