package org.example.sdt_project_2.repository;

import org.example.sdt_project_2.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query("SELECT t FROM Task t WHERE t.isMilestone = true")
    List<Task> findMilestones();
}
