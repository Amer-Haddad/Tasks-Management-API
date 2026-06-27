package com.example.Tasks.services.impl;

import com.example.Tasks.domain.entities.Task;
import com.example.Tasks.domain.entities.TaskList;
import com.example.Tasks.domain.entities.TaskPriority;
import com.example.Tasks.domain.entities.TaskStatus;
import com.example.Tasks.repositories.TaskListRepository;
import com.example.Tasks.repositories.TaskRepository;
import com.example.Tasks.services.TaskService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskListRepository taskListRepository;

    public TaskServiceImpl(TaskRepository taskRepository, TaskListRepository taskListRepository) {
        this.taskRepository = taskRepository;
        this.taskListRepository = taskListRepository;
    }

    @Override
    public List<Task> listTasks(UUID taskListId) {
        return taskRepository.findByTaskListId(taskListId);
    }

    @Override
    public Task createTask(UUID taskListId, Task task) {
        if(null != task.getId()) {
            throw new IllegalArgumentException("Task already has an ID!");
        }
        if(null == task.getTitle() || task.getTitle().isBlank()) {
            throw new IllegalArgumentException("Task must have a title!");
        }

        TaskPriority taskPriority = Optional.ofNullable(task.getPriority())
                .orElse(TaskPriority.MEDIUM);

        TaskStatus taskStatus = TaskStatus.OPEN;

        TaskList taskList = taskListRepository.findById(taskListId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Task List ID provided!"));

        LocalDateTime now = LocalDateTime.now();
        Task taskToSave = new Task(
                null,
                task.getTitle(),
                task.getDescription(),
                task.getDueDate(),
                taskStatus,
                taskPriority,
                taskList,
                now,
                now
        );

        return taskRepository.save(taskToSave);
    }

    @Override
    public Task updateTask(UUID taskListId, UUID taskId, Task task) {
        if (task.getId() == null || !task.getId().equals(taskId)) {
            throw new IllegalArgumentException("Task ID mismatch!");
        }
        if (task.getTitle() == null || task.getTitle().isBlank()) {
            throw new IllegalArgumentException("Task must have a title!");
        }

        Task existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found!"));

        // Verify the task belongs to the specified task list
        if (!existingTask.getTaskList().getId().equals(taskListId)) {
            throw new IllegalArgumentException("Task does not belong to the specified task list!");
        }

        TaskPriority taskPriority = Optional.ofNullable(task.getPriority())
                .orElse(existingTask.getPriority());
        TaskStatus taskStatus = task.getStatus() != null ? task.getStatus() : existingTask.getStatus();

        Task updatedTask = new Task(
                taskId,
                task.getTitle(),
                task.getDescription(),
                task.getDueDate(),
                taskStatus,
                taskPriority,
                existingTask.getTaskList(),
                existingTask.getCreated(),
                LocalDateTime.now()
        );

        return taskRepository.save(updatedTask);
    }

    @Override
    public void deleteTask(UUID taskListId, UUID taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found!"));

        // Verify the task belongs to the specified task list
        if (!task.getTaskList().getId().equals(taskListId)) {
            throw new IllegalArgumentException("Task does not belong to the specified task list!");
        }

        taskRepository.deleteById(taskId);
    }
}