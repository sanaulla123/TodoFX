package net.javabeat.db;

import com.mongodb.*;
import org.bson.types.ObjectId;

import java.net.UnknownHostException;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: sanaulla
 * Date: 8/5/12
 * Time: 11:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class TodoDAO {

    private static final String DBNAME = "todoapp";
    private static final String COLLECTION_NAME = "todo";
    public static void saveTodo(Todo todo) throws UnknownHostException{
        DBObject dbObject = BasicDBObjectBuilder.start()
                .add("task",todo.getTask())
                .add("completed",todo.isCompleted())
                .add("added",todo.getAdded())
                .get();
        DB db = DbManager.getDb(DBNAME);
        DBCollection dbCollection = db.getCollection(COLLECTION_NAME);
        dbCollection.save(dbObject);

    }

    public static List<Todo> getAllTodos() throws UnknownHostException{
        DB db = DbManager.getDb(DBNAME);

        DBCollection dbCollection = db.getCollection(COLLECTION_NAME);
        DBCursor dbCursor = dbCollection.find();
        List<Todo> allTodos = new ArrayList<Todo>();
        while ( dbCursor.hasNext()){
            DBObject dbObject = dbCursor.next();
            String task = String.valueOf(dbObject.get("task"));
            Date added = (Date)dbObject.get("added");
            boolean completed = (Boolean)dbObject.get("completed");
            System.out.println(dbObject.get("_id")+" Type: "+dbObject.get("_id").getClass().getName());
            Todo todo = new Todo(task,completed, added, dbObject.get("_id").toString());
            allTodos.add(todo);
        }

        return allTodos;

    }
    
    public static List<Todo> getOpenTodos() throws UnknownHostException{
        DB db = DbManager.getDb(DBNAME);
        DBCollection collection = db.getCollection(COLLECTION_NAME);
        DBObject filterObject = BasicDBObjectBuilder.start()
                .add("completed",false)
                .get();
        DBCursor dbCursor = collection.find(filterObject);
        List<Todo> openTodos = new ArrayList<Todo>();
        while ( dbCursor.hasNext()){
            DBObject dbObject = dbCursor.next();
            String task = String.valueOf(dbObject.get("task"));
            Date added = (Date)dbObject.get("added");
            boolean completed = (Boolean)dbObject.get("completed");
            System.out.println(dbObject.get("_id")+" Type: "+dbObject.get("_id").getClass().getName());
            Todo todo = new Todo(task,completed, added, dbObject.get("_id").toString());
            openTodos.add(todo);
        }

        return openTodos;

    }

    public static void setTodoAsCompleted(Todo todoRef) throws UnknownHostException{
        DB db  = DbManager.getDb(DBNAME);
        DBCollection collection = db.getCollection(COLLECTION_NAME);
        DBObject queryObject = BasicDBObjectBuilder.start()
                .add("_id", new ObjectId(todoRef.getId()))
                .get();
        DBObject updateValue = BasicDBObjectBuilder.start()
                .add("completed",true)
                .get();

        collection.update(queryObject,updateValue);
    }
}
