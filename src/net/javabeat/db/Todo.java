package net.javabeat.db;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: sanaulla
 * Date: 8/5/12
 * Time: 11:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class Todo {
    public Todo(String task,
                boolean completed,
                Date added,
                String id){
        this.task = task;
        this.completed = completed;
        this.added = added;
        this.setId(id);
    }
    
    public Todo(String task){
        this.task       = task;
        this.added      = new Date();
        this.completed  = false;
    }
    
    @Override
    public String toString(){
        return this.getAdded()+": "+this.getTask();
    }
    
    private String task;
    private boolean completed;
    private Date added;
    private Date finished;
    private String id;

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Date getAdded() {
        return added;
    }

    public Date getFinished() {
        return finished;
    }

    public void setFinished(Date finished) {
        this.finished = finished;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
