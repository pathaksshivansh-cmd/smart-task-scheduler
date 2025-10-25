package com.example.scheduler;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.Timer;
import java.util.TimerTask;

public class TaskManager {

    private PriorityBlockingQueue<Task> taskQueue;
    private static final String FILE_PATH = System.getProperty("user.home") + File.separator + ".smarttasks" + File.separator + "tasks.json";
    private Gson gson;
    private Timer reminderTimer;

    public TaskManager() {
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeAdapter())
                .setPrettyPrinting()
                .create();

        taskQueue = new PriorityBlockingQueue<>();
        loadTasks();
    }

    // 🟢 Add new task
    public void addTask(Task task) {
        taskQueue.add(task);
        saveTasks();
    }

    // 🔴 Delete task
    public void removeTask(Task task) {
        taskQueue.remove(task);
        saveTasks();
    }

    // 📋 Get all tasks (sorted)
    public List<Task> getTasks() {
        List<Task> list = new ArrayList<>(taskQueue);
        Collections.sort(list);
        return list;
    }

    // 💾 Save tasks to JSON file
    private void saveTasks() {
        try {
            File dir = new File(FILE_PATH).getParentFile();
            if (!dir.exists()) dir.mkdirs();

            Writer writer = Files.newBufferedWriter(Paths.get(FILE_PATH));
            gson.toJson(new ArrayList<>(taskQueue), writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 📂 Load tasks from JSON file
    private void loadTasks() {
        try {
            Path path = Paths.get(FILE_PATH);
            if (!Files.exists(path)) return;

            Reader reader = Files.newBufferedReader(path);
            Type listType = new TypeToken<ArrayList<Task>>() {}.getType();
            List<Task> loadedTasks = gson.fromJson(reader, listType);
            reader.close();

            if (loadedTasks != null) taskQueue.addAll(loadedTasks);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ⏰ Start reminders
    public void startReminders() {
        reminderTimer = new Timer(true);
        reminderTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                for (Task task : new ArrayList<>(taskQueue)) {
                    if (!task.isCompleted() && task.getDeadline().isBefore(LocalDateTime.now())) {
                        System.out.println("🔔 Reminder: " + task.getTitle() + " ka deadline ho gaya!");
                        task.setCompleted(true);
                        saveTasks();
                    }
                }
            }
        }, 0, 60000); // har 1 minute me check karega
    }
}
