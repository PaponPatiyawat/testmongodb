/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

/**
 *
 * @author delta
 */
public class DBAuthority {
    
    static boolean Authority(String UserID,String DBName,String CollName){
        MongoClient mongoClient = new MongoClient("localhost",27017);
        MongoDatabase db = mongoClient.getDatabase("test");
        MongoCollection<Document> coll = db.getCollection("authority");
        
        return coll.find(Document.parse("{id:\""+UserID+"\",DB:\""+DBName+"\",collections:\""+CollName+"\"}")).iterator().hasNext();
        
    }
    
}
