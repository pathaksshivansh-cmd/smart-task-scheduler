package com.example.scheduler;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.time.LocalDateTime;

public class MainApp extends Application {

    private TaskManager taskManager;
    private ListView<Task> taskListView;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        taskManager = new TaskManager();
        taskManager.startReminders(); // start reminder timer

        // Layout
        VBox root = new VBox(10);
        root.setPadding(new javafx.geometry.Insets(10));

        // Task list
        taskListView = new ListView<>();
        refreshTaskList();

        // Input fields
        TextField titleField = new TextField();
        titleField.setPromptText("Task title");

        TextField priorityField = new TextField();
        priorityField.setPromptText("Priority (1-10)");

        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Deadline Date");

        TextField hourField = new TextField();
        hourField.setPromptText("Hour (0-23)");

        TextField minuteField = new TextField();
        minuteField.setPromptText("Minute (0-59)");

        Button addButton = new Button("Add Task");
        addButton.setOnAction(e -> {
            try {
                String title = titleField.getText();
                int priority = Integer.parseInt(priorityField.getText());
                LocalDateTime deadline = datePicker.getValue().atTime(
                        Integer.parseInt(hourField.getText()),
                        Integer.parseInt(minuteField.getText())
                );
                Task task = new Task(title, priority, deadline);
                taskManager.addTask(task);
                refreshTaskList();
            } catch (Exception ex) {
                showAlert("Error", "Invalid input. Please check values.");
            }
        });

        Button deleteButton = new Button("Delete Selected Task");
        deleteButton.setOnAction(e -> {
            Task selected = taskListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                taskManager.removeTask(selected);
                refreshTaskList();
            }
        });

        // Layout for input
        HBox inputBox = new HBox(5, titleField, priorityField, datePicker, hourField, minuteField, addButton, deleteButton);

        root.getChildren().addAll(taskListView, inputBox);

        Scene scene = new Scene(root, 900, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Smart Task Scheduler");
        primaryStage.show();
    }

    private void refreshTaskList() {
        taskListView.getItems().clear();
        taskListView.getItems().addAll(taskManager.getTasks());
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
