package org.example.reporting_and_analytics.service;

import org.example.reporting_and_analytics.entity.Report;
import org.example.reporting_and_analytics.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportService {
    @Autowired
    private final RestTemplate restTemplate;

    public ReportService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Report calculateProgress() {
        String taskApiUrl = "http://task-management-service/tasks";
        List<Task> tasks = restTemplate.exchange(taskApiUrl, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Task>>() {}).getBody();

        long totalTasks = tasks.size();
        long completedTasks = tasks.stream()
                .filter(task -> "Completed".equalsIgnoreCase(task.getStatus()))
                .count();

        double progressPercentage = (totalTasks > 0) ? (completedTasks * 100.0 / totalTasks) : 0;

        Map<String, Object> progressDetails = new HashMap<>();
        progressDetails.put("totalTasks", totalTasks);
        progressDetails.put("completedTasks", completedTasks);
        progressDetails.put("progressPercentage", progressPercentage);

        return new Report("Project Progress", progressDetails);
    }


    public Report calculateTimelines() {
        String taskApiUrl = "http://task-management-service/tasks";
        List<Task> tasks = restTemplate.exchange(taskApiUrl, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Task>>() {}).getBody();

        LocalDateTime now = LocalDateTime.now();

        long onTimeTasks = tasks.stream()
                .filter(task -> "Completed".equalsIgnoreCase(task.getStatus()) && task.getDeadline().isAfter(now))
                .count();

        long overdueTasks = tasks.stream()
                .filter(task -> !"Completed".equalsIgnoreCase(task.getStatus()) && task.getDeadline().isBefore(now))
                .count();

        Map<String, Object> milestoneDetails = new HashMap<>();
        milestoneDetails.put("onTimeTasks", onTimeTasks);
        milestoneDetails.put("overdueTasks", overdueTasks);

        return new Report("Timeline Report", milestoneDetails);
    }


    public Report calculateTeamPerformance() {
        String taskApiUrl = "http://task-management-service/tasks";
        List<Task> tasks = restTemplate.exchange(taskApiUrl, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Task>>() {}).getBody();

        Map<String, Long> performanceDetails = tasks.stream()
                .filter(task -> "Completed".equalsIgnoreCase(task.getStatus()))
                .collect(Collectors.groupingBy(Task::getAssignee, Collectors.counting()));

        return new Report("Team Performance Report", performanceDetails);
    }


    public Map<String, Object> getMilestoneOverview() {
        String taskApiUrl = "http://task-management-service/tasks/milestones";
        List<Task> milestones = restTemplate.exchange(taskApiUrl, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Task>>() {}).getBody();

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

    public Map<String, Object> getTaskCompletionRates() {
        // Fetch all tasks from the Task Microservice
        String taskApiUrl = "http://task-management-service/tasks";
        List<Task> tasks = restTemplate.exchange(taskApiUrl, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Task>>() {}).getBody();

        if (tasks == null || tasks.isEmpty()) {
            throw new RuntimeException("No tasks available to calculate completion rates.");
        }

        long totalTasks = tasks.size();
        long completedTasks = tasks.stream()
                .filter(task -> "Completed".equalsIgnoreCase(task.getStatus()))
                .count();

        double completionRate = (double) completedTasks / totalTasks * 100;

        Map<String, Object> completionRateDetails = new HashMap<>();
        completionRateDetails.put("totalTasks", totalTasks);
        completionRateDetails.put("completedTasks", completedTasks);
        completionRateDetails.put("completionRate", completionRate);

        return completionRateDetails;
    }
}
