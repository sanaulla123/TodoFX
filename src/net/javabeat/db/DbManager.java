package net.javabeat.db;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;

import java.net.UnknownHostException;

/**
 * Created by IntelliJ IDEA.
 * User: sanaulla
 * Date: 8/5/12
 * Time: 11:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class DbManager {

    private static DB db;
    public static DB getDb (String name) throws UnknownHostException {
        Mongo mongo = new Mongo();
        if ( db == null){
            db = mongo.getDB(name);
        }
        return db;
    }

}
