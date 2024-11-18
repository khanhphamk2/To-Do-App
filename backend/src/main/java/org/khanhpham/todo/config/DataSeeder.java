package org.khanhpham.todo.config;

import org.khanhpham.todo.entity.Task;
import org.khanhpham.todo.entity.User;
import org.khanhpham.todo.repository.TaskRepository;
import org.khanhpham.todo.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The {@code DataSeeder} class seeds the initial set of users and tasks into the database.
 * It checks if the {@link UserRepository} and {@link TaskRepository} are empty,
 * and if so, populates them with 100 users and 30 tasks, respectively.
 */
@Component
public class DataSeeder implements CommandLineRunner {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final Random random;

    /**
     * Constructs a new {@code DataSeeder} with the specified repositories.
     *
     * @param taskRepository the repository to manage {@link Task} entities
     * @param userRepository the repository to manage {@link User} entities
     */
    public DataSeeder(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.random = new Random();
    }

    /**
     * Runs the data seeding process when the application starts.
     * It seeds users and tasks into the database if they do not already exist.
     *
     * @param args the command-line arguments passed to the application
     */
    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            seedUsers();
            System.out.println("Seeded 100 users!");
        }

        if (taskRepository.count() == 0) {
            List<Task> tasks = new ArrayList<>();

            for (int i = 1; i <= 30; i++) {
                Task task = new Task();
                task.setTitle("Task #" + i);
                task.setDescription("This is the description for task #" + i);
                task.setDate(LocalDate.now().plusDays((long) i + 5));
                task.setTime(LocalDateTime.now().plusHours((long) i + 3).toLocalTime());
                task.setCompleted(false);
                task.setCreatedDate(LocalDateTime.now());
                task.setUpdatedDate(LocalDateTime.now());
                task.setUser(userRepository.findById((long) random.nextInt(100) + 1).orElse(null));

                tasks.add(task);
            }

            taskRepository.saveAll(tasks);
            System.out.println("Seeded 30 tasks with audit info!");
        }
    }

    /**
     * Seeds 100 users into the {@link UserRepository}.
     * It generates 100 unique users and saves them to the database.
     */
    public void seedUsers() {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            users.add(generateRandomUser());
        }
        userRepository.saveAll(users);
    }

    /**
     * Generates a random {@link User} with a unique email and username.
     *
     * @return a randomly generated {@code User}
     */
    private User generateRandomUser() {
        User user = new User();
        String sharedPassword = "$2a$10$XIfvNt9E0Q61.vjYYMsMzOWVNgkXwL9Gy03UQqVNJC3g2bm0N5Veq"; // password: admin@123
        user.setEmail(generateUniqueEmail());
        user.setUsername(generateUniqueUsername());
        user.setPassword(sharedPassword);
        return user;
    }

    /**
     * Generates a unique username that does not already exist in the database.
     *
     * @return a unique username
     */
    private String generateUniqueUsername() {
        String username;
        do {
            username = generateRandomUsername();
        } while (Boolean.TRUE.equals(userRepository.existsByUsername(username)));
        return username;
    }

    /**
     * Generates a unique email that does not already exist in the database.
     *
     * @return a unique email address
     */
    private String generateUniqueEmail() {
        String email;
        do {
            email = generateRandomEmail();
        } while (Boolean.TRUE.equals(userRepository.existsByEmail(email)));
        return email;
    }

    /**
     * Retrieves a random element from an array of elements.
     *
     * @param array the array of elements to choose from
     * @param <T>   the type of elements in the array
     * @return a randomly selected element from the array
     */
    private <T> T getRandomElement(T[] array) {
        return array[random.nextInt(array.length)];
    }

    /**
     * Generates a random username by combining random prefixes and suffixes.
     *
     * @return a randomly generated username
     */
    private String generateRandomUsername() {
        String[] prefixes = {
                "user", "player", "member", "guest", "admin", "super", "mega", "cool", "awesome", "ultimate",
                "pro", "gamergirl", "rockstar", "darkknight", "theone", "cyber", "tech", "x", "fire", "ice",
                "storm", "pixel", "dream", "fusion", "byte", "planet", "wave", "phoenix", "nova", "spirit",
                "shadow", "galaxy", "infinity", "cosmic", "neon", "electric", "mystic", "crimson", "sunrise",
                "twilight"
        };
        String[] suffixes = {
                "123", "456", "789", "abc", "xyz", "00", "11", "22", "33", "44", "55", "66", "77", "88",
                "99", "alpha", "beta", "omega", "ace","max", "prime", "nova", "quantum", "wave", "flux",
                "vortex", "neutron", "quantum", "pulse", "echo", "flare", "surge", "crux", "nexus", "forge",
                "strive", "impact", "spark", "raven", "jolt", "lunar", "titan", "comet", "orbit", "velocity",
                "radiant", "blaze", "thunder", "flash", "dynamo", "zenith", "crest", "pulse", "drift", "glow",
                "quest", "vertex", "edge"
        };
        return getRandomElement(prefixes) + getRandomElement(suffixes);
    }

    /**
     * Generates a random email address by combining random prefixes and domains.
     *
     * @return a randomly generated email address
     */
    private String generateRandomEmail() {
        String[] domains = { "gmail.com", "yahoo.com", "hotmail.com", "outlook.com", "aol.com", "example.com", "test.com", "domain.com", "mail.com", "myemail.com",
                "company.com", "school.edu", "website.net", "service.org", "shop.co", "business.io", "tech.ai", "blog.xyz", "info.me", "support.dev",
                "workplace.us", "network.tv", "platform.app", "studio.fm", "store.store", "game.games", "music.audio", "video.video", "photo.pics",
                "social.social", "market.market", "news.news", "travel.travel", "food.food", "health.health", "sports.sports", "book.books",
                "art.art", "science.science", "finance.finance", "fashion.fashion", "fitness.fitness", "home.home", "tech.tech", "design.design",
                "gaming.gaming", "movie.movies", "car.cars", "animal.animals", "nature.nature", "business.business", "education.education", "foodie.foodie",
                "healthcare.healthcare", "technology.technology", "traveler.traveler", "sportslover.sportslover", "musiclover.musiclover", "artlover.artlover",
                "gamer.gamer", "photographer.photographer" };
        String[] prefixes = { "john", "jane", "smith", "doe", "alex", "emma", "david", "olivia", "michael", "sophia", "samuel", "lily", "jacob", "ava", "liam",
                "mia", "ethan", "noah", "charlotte", "benjamin", "grace", "henry", "lucy", "oliver", "amelia", "james", "ella", "william", "harper",
                "jackson", "scarlett", "andrew", "zoey", "matthew", "avery", "daniel", "hannah", "luke", "elizabeth", "christopher", "natalie", "owen",
                "claire", "nathan", "addison", "gabriel", "aria", "sebastian", "zoey", "julian", "claire", "ryan", "abigail", "joseph", "chloe",
                "jonathan", "madison", "isaiah", "ella", "carter", "audrey", "evan", "skylar", "christian", "piper", "nicholas", "riley" };
        String[] suffixes = { "123", "456", "789", "abc", "xyz", "00", "11", "22", "33", "44", "55", "66", "77", "88", "99", "qwerty", "test", "user", "random",
                "alpha", "beta", "omega", "gamer", "expert", "master", "ninja", "warrior", "king", "queen", "star", "wizard", "genius", "champion",
                "hero", "legend", "hunter", "pro", "savage", "beast", "guru", "viper", "sprinter", "phantom", "pioneer", "tiger", "wolf", "eagle",
                "hawk", "lion", "panther", "ace", "cyber", "tech", "x", "fire", "ice", "storm", "pixel", "dream", "fusion", "byte", "planet", "wave" };

        return getRandomElement(prefixes) + getRandomElement(suffixes) + "@" + getRandomElement(domains);
    }
}
