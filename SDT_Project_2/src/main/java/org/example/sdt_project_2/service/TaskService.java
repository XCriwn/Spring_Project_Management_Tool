package org.example.sdt_project_2.service;

import org.example.sdt_project_2.entity.Task;
import org.example.sdt_project_2.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TaskService {
    @Autowired
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task updateTask(Long id, Task updatedTask) {
        return taskRepository.findById(id)
                .map(task -> {
                    if (updatedTask.getName() != null) {
                        task.setName(updatedTask.getName());
                    }
                    if (updatedTask.getDescription() != null) {
                        task.setDescription(updatedTask.getDescription());
                    }
                    if (updatedTask.getAssignee() != null) {
                        task.setAssignee(updatedTask.getAssignee());
                    }
                    if (updatedTask.getPriority() != null) {
                        task.setPriority(updatedTask.getPriority());
                    }
                    if (updatedTask.getStatus() != null) {
                        task.setStatus(updatedTask.getStatus());
                    }
                    if (updatedTask.getDeadline() != null) {
                        task.setDeadline(updatedTask.getDeadline());
                    }
                    if (updatedTask.getCompletionDate() != null) {
                        task.setCompletionDate(updatedTask.getCompletionDate());
                    }
                    if (updatedTask.getParentTask() != null) {
                        task.setParentTask(updatedTask.getParentTask());
                    }
                    if (updatedTask.getSubtasks() != null) {
                        task.setSubtasks(updatedTask.getSubtasks());
                    }
                    if (updatedTask.getDependencies() != null) {
                        task.setDependencies(updatedTask.getDependencies());
                    }
                    return taskRepository.save(task);
                })
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }

    //PART 2
    public Task addSubtask(Long parentTaskId, Task subtask) {
        Task parentTask = taskRepository.findById(parentTaskId)
                .orElseThrow(() -> new RuntimeException("Parent task not found"));

        subtask.setParentTask(parentTask);
        parentTask.getSubtasks().add(subtask);

        return taskRepository.save(parentTask);
    }

    public Task addDependency(Long taskId, Long dependencyId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        Task dependency = taskRepository.findById(dependencyId)
                .orElseThrow(() -> new RuntimeException("Dependency task not found"));

        task.getDependencies().add(dependency);

        return taskRepository.save(task);
    }

    public Map<String, Object> getDeadlineOverview() {
        List<Task> tasks = taskRepository.findAll();
        LocalDateTime now = LocalDateTime.now();

        long upcomingTasks = tasks.stream()
                .filter(task -> task.getDeadline() != null && task.getDeadline().isAfter(now) && task.getDeadline().isBefore(now.plusDays(7)))
                .count();

        long overdueTasks = tasks.stream()
                .filter(task -> task.getDeadline() != null && task.getDeadline().isBefore(now) && !"Completed".equalsIgnoreCase(task.getStatus()))
                .count();

        Map<String, Object> deadlineOverview = new HashMap<>();
        deadlineOverview.put("upcomingTasks", upcomingTasks);
        deadlineOverview.put("overdueTasks", overdueTasks);

        return deadlineOverview;
    }

    public Map<String, Object> getMilestoneOverview() {
        List<Task> milestones = taskRepository.findMilestones(); // Custom query for milestone tasks
        LocalDateTime now = LocalDateTime.now();

        long completedMilestones = milestones.stream()
                .filter(task -> "Completed".equalsIgnoreCase(task.getStatus()))
                .count();

        long pendingMilestones = milestones.stream()
                .filter(task -> !"Completed".equalsIgnoreCase(task.getStatus()))
                .count();

        Map<String, Object> milestoneOverview = new HashMap<>();
        milestoneOverview.put("completedMilestones", completedMilestones);
        milestoneOverview.put("pendingMilestones", pendingMilestones);

        return milestoneOverview;
    }

    public Map<String, Object> getCompletionPerformance() {
        List<Task> completedTasks = taskRepository.findAll().stream()
                .filter(task -> "Completed".equalsIgnoreCase(task.getStatus()))
                .collect(Collectors.toList());

        long onTimeTasks = completedTasks.stream()
                .filter(task -> task.getCompletionDate() != null && task.getCompletionDate().isBefore(task.getDeadline()))
                .count();

        long delayedTasks = completedTasks.stream()
                .filter(task -> task.getCompletionDate() != null && task.getCompletionDate().isAfter(task.getDeadline()))
                .count();

        Map<String, Object> performanceMetrics = new HashMap<>();
        performanceMetrics.put("onTimeTasks", onTimeTasks);
        performanceMetrics.put("delayedTasks", delayedTasks);

        return performanceMetrics;
    }

    public List<Task> findMilestones() {
        return taskRepository.findMilestones();
    }




}

