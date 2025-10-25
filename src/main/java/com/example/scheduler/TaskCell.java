package com.example.scheduler;

import javafx.scene.control.ListCell;

public class TaskCell extends ListCell<Task> {
    @Override
    protected void updateItem(Task task, boolean empty) {
        super.updateItem(task, empty);
        if (empty || task == null) {
            setText(null);
        } else {
            setText(task.toString());
        }
    }
}


