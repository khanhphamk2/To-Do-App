package org.khanhpham.todo.repository;

import org.khanhpham.todo.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findTaskByUserId(Long userId);
    List<Task> findByUserIdAndIsCompleted(Long userId, boolean isCompleted);
    List<Task> findByUserIdAndIsImportant(Long userId, boolean isImportant);
}