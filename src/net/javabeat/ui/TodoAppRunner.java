package net.javabeat.ui;

import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.ImageViewBuilder;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import net.javabeat.db.Todo;
import net.javabeat.db.TodoDAO;

import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: sanaulla
 * Date: 8/5/12
 * Time: 11:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class TodoAppRunner extends Application{
    
    public static final int APP_WIDTH = 220;
    public static final int APP_HEIGHT = 300;

    public static void main(String[] args) throws UnknownHostException{

//        Todo todo = new Todo("Task 1 from Command Line");
//        TodoDAO.saveTodo(todo);
//        todo = new Todo("Task 2 from Command Line");
//        TodoDAO.saveTodo(todo);
//        todo = new Todo("Task 3 from Command Line");
//        TodoDAO.saveTodo(todo);
//
//        List<Todo> allTodos = TodoDAO.getAllTodos();
//        for ( Todo aTodo : allTodos){
//            System.out.println(aTodo);
//        }
//        TodoDAO.setTodoAsCompleted(allTodos.get(0));
//        allTodos = TodoDAO.getOpenTodos();
//        for ( Todo aTodo : allTodos){
//            System.out.println(aTodo);
//        }

       Application.launch(args);

    }
    
    List<Todo> openTodos;
    @Override
    public void start(Stage stage) throws Exception {
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, APP_WIDTH,APP_HEIGHT);
        Tab addNewTab = buildAddNewTodoUi();
        Tab allTodosTab = buildShowAllTodoUi();

        TabPane tabPane = TabPaneBuilder.create()
                .tabs(addNewTab, allTodosTab)
                .build();

        root.setCenter(tabPane);
        stage.setScene(scene);
        stage.setTitle("My Todo's!");
        stage.setResizable(false);
        stage.show();
    }

    private Tab buildAddNewTodoUi(){
        Label msgLabel = LabelBuilder.create()
                .text("")
                .visible(false)
                .build();

        TextField tNameField = TextFieldBuilder.create()
                .build();
        final StringProperty todoTitle = tNameField.textProperty();
        final StringProperty labelMsg = msgLabel.textProperty();
        final BooleanProperty labelVisibility = msgLabel.visibleProperty();

        Label tNameLabel = LabelBuilder.create()
                .text("Task")
                .build();
        Button addNewButton = ButtonBuilder.create()
                .text("Add")
                .onAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        Todo todo = new Todo(todoTitle.get());
                        todoTitle.set("");
                        try {
                            TodoDAO.saveTodo(todo);
                            labelMsg.set("Successfully added the task");
                            labelVisibility.set(true);
                        } catch (UnknownHostException ex) {
                            ex.printStackTrace();
                        }
                    }
                })
                .build();
        GridPane addNewPane = GridPaneBuilder.create()
                .hgap(5)
                .vgap(5)
                .build();
        
        addNewPane.add(msgLabel,1,2,2,1);
        addNewPane.add(tNameLabel,1,3);
        addNewPane.add(tNameField,2,3);
        addNewPane.add(addNewButton,2,5);

        Tab addNewTab = TabBuilder.create()
                .content(addNewPane)
                .text("Add Task")
                .closable(false)
                .build();

        return addNewTab;

    }

    private Tab buildShowAllTodoUi(){
        final ScrollPane todoListScroll = ScrollPaneBuilder.create()
                .hbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED)
                .prefHeight(APP_HEIGHT - 75)
                .maxWidth(APP_WIDTH)
                .build();

        final VBox todoBox = new VBox();
        todoListScroll.setContent(todoBox);

        Tab allTodosTab = TabBuilder.create()
                .text("All todos")

                .content(todoListScroll)
                .closable(false)
                .onSelectionChanged(
                        new TodoLoader(todoBox)
                )
                .build();

        return allTodosTab;
    }
}

class TodoLoader implements EventHandler<Event>{

    List<Todo> openTodos;   
    VBox todoBox;
    
    TodoLoader(VBox todoBox){
        this.todoBox = todoBox;
    }
    @Override
    public void handle(Event event) {
        //Clear the existing Todos, for adding new Todos
        todoBox.getChildren().clear();
        try {
            openTodos = TodoDAO.getOpenTodos();
            for(Todo aTodo : openTodos){
                GridPane todoPane = new GridPane();

                Label taskLabel = new Label(aTodo.getTask());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("");
                Label dateAdded = new Label(aTodo.getAdded().toString());
                CloseTodoHandler closeHandler =
                        new CloseTodoHandler(todoBox,
                                todoPane,
                                aTodo);
                Button closeButton  = ButtonBuilder.create()
                        .graphic(new ImageView(
                                new Image(getClass().getResourceAsStream("check.png"))))
                        .onAction(closeHandler)
                        .build();

                todoPane.add(taskLabel,1,1,5,1);
                todoPane.add(dateAdded,1,2,4,1);
                todoPane.add(closeButton, 5, 2, 1, 1);
                todoBox.setSpacing(10);
                todoBox.getChildren().add(todoPane);

            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }


    }
}

class CloseTodoHandler implements EventHandler<ActionEvent>{

    VBox todoBox;
    GridPane todoPane;
    Todo todo;

    CloseTodoHandler(VBox todoBox,
                     GridPane todoPane,
                     Todo todo){
        this.todo = todo;
        this.todoBox = todoBox;
        this.todoPane = todoPane;
    }
    @Override
    public void handle(ActionEvent actionEvent) {
        try {
            TodoDAO.setTodoAsCompleted(todo);
            todoBox.getChildren().remove(todoPane);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }
}