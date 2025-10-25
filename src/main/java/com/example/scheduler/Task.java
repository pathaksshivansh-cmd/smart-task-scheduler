package com.example.scheduler;

import java.time.LocalDateTime;

public class Task implements Comparable<Task> {
    private String title;
    private int priority;
    private LocalDateTime deadline;
    private boolean completed;

    public Task(String title, int priority, LocalDateTime deadline) {
        this.title = title;
        this.priority = priority;
        this.deadline = deadline;
        this.completed = false;
    }

    // Getters
    public String getTitle() { return title; }
    public int getPriority() { return priority; }
    public LocalDateTime getDeadline() { return deadline; }
    public boolean isCompleted() { return completed; }

    // Setters
    public void setCompleted(boolean completed) { this.completed = completed; }

    // For PriorityBlockingQueue sorting (higher priority first)
    @Override
    public int compareTo(Task other) {
        return Integer.compare(other.priority, this.priority);
    }

    @Override
    public String toString() {
        return "Task{" +
                "title='" + title + '\'' +
                ", priority=" + priority +
                ", deadline=" + deadline +
                ", completed=" + completed +
                '}';
    }
}
