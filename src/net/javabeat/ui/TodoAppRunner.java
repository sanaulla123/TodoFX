package net.javabeat.ui;

import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
    
    public static final int APP_WIDTH = 200;
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
    static Integer id = 1;
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
        final ObservableList<Object> allTodos =
                FXCollections.observableArrayList();

        final ScrollPane todoListScroll = ScrollPaneBuilder.create()
                .hbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED)
                .build();

        Button refreshTodos = ButtonBuilder.create()
                .text("Refresh Todo's")
                .onAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        allTodos.clear();
                        try {
                            List<Todo> todos = TodoDAO.getOpenTodos();
                            GridPane todoBox;
                            VBox todoListBox = new VBox(5);
                            todoListBox.autosize();
                            Image image = new Image(getClass().getResourceAsStream("check.png"));
                            for (final Todo aTodo : todos) {
                                Button doneButton = ButtonBuilder.create()
                                        .graphic(new ImageView(image))
                                        .onAction(new EventHandler<ActionEvent>() {
                                            @Override
                                            public void handle(ActionEvent actionEvent) {
                                                System.out.println("Clearning: " + aTodo);
                                            }
                                        })
                                        .build();

                                Label taskLabel = LabelBuilder.create()
                                        .text(aTodo.getTask())
                                        .wrapText(true)
                                        .build();
                                Label postedOn = LabelBuilder.create()
                                        .text(aTodo.getAdded().toString())
                                        .build();

                                todoBox = new GridPane();
                                todoBox.add(postedOn,1,1);
                                todoBox.add(taskLabel,1,2);
                                todoBox.add(doneButton,3,2);

                                todoListBox.getChildren().add(todoBox);


                            }
                            todoListScroll.setContent(todoListBox);

                        } catch (UnknownHostException ex) {
                            ex.printStackTrace();
                        }
                    }
                })
                .build();
        VBox allTodosBox = VBoxBuilder.create()
                .children(refreshTodos,todoListScroll)
                .spacing(10)
                .translateY(10)
                .translateX(10)
                .build();

        Tab allTodosTab = TabBuilder.create()
                .text("All todos")
                .content(allTodosBox)
                .closable(false)
                .build();

        return allTodosTab;
    }
}