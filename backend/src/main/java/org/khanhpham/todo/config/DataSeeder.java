package org.khanhpham.todo.config;

import org.khanhpham.todo.entity.Task;
import org.khanhpham.todo.repository.TaskRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {
    private final TaskRepository taskRepository;

    public DataSeeder(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /**
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        if (taskRepository.count() == 0) {
            List<Task> tasks = new ArrayList<>();

            for (int i = 1; i <= 30; i++) {
                Task task = new Task();
                task.setTask("Task #" + i);
                task.setDescription("This is the description for task #" + i);
                task.setDeadline(LocalDateTime.now().plusDays(i+5));
                task.setCompleted(false);

                task.setCreatedBy("Seeder");
                task.setCreatedDate(LocalDateTime.now());
                task.setUpdatedDate(LocalDateTime.now());

                tasks.add(task);
            }

            taskRepository.saveAll(tasks);
            System.out.println("Seeded 30 tasks with audit info!");
        }
    }
}
