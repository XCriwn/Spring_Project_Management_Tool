package org.example.sdt_project_2.controller;

import org.example.sdt_project_2.entity.Task;
import org.example.sdt_project_2.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(task));
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task task) {
        return ResponseEntity.ok(taskService.updateTask(id, task));
    }

    @PostMapping("/{id}/subtasks")
    public ResponseEntity<Task> addSubtask(@PathVariable Long id, @RequestBody Task subtask) {
        return ResponseEntity.ok(taskService.addSubtask(id, subtask));
    }

    @PostMapping("/{id}/dependencies/{dependencyId}")
    public ResponseEntity<Task> addDependency(@PathVariable Long id, @PathVariable Long dependencyId) {
        return ResponseEntity.ok(taskService.addDependency(id, dependencyId));
    }

    @GetMapping("/milestones")
    public ResponseEntity<List<Task>> getMilestones() {
        return ResponseEntity.ok(taskService.findMilestones());
    }


}


