package de.unibayreuth.se.taskboard;

import de.unibayreuth.se.taskboard.api.dtos.TaskDto;
import de.unibayreuth.se.taskboard.api.dtos.UserDto;
import de.unibayreuth.se.taskboard.api.mapper.TaskDtoMapper;
import de.unibayreuth.se.taskboard.api.mapper.UserDtoMapper;
import de.unibayreuth.se.taskboard.business.domain.Task;
import de.unibayreuth.se.taskboard.business.domain.User;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;


public class TaskBoardSystemTests extends AbstractSystemTest {

    @Autowired
    private TaskDtoMapper taskDtoMapper;
    @Autowired
    private UserDtoMapper userDtoMapper;

    @Test
    void getAllCreatedTasks() {
        List<Task> createdTasks = TestFixtures.createTasks(taskService);

        List<Task> retrievedTasks = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/tasks")
                .then()
                .statusCode(200)
                .body(".", hasSize(createdTasks.size()))
                .and()
                .extract().jsonPath().getList("$", TaskDto.class)
                .stream()
                .map(taskDtoMapper::toBusiness)
                .toList();

        assertThat(retrievedTasks)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("createdAt", "updatedAt") // prevent issues due to differing timestamps after conversions
                .containsExactlyInAnyOrderElementsOf(createdTasks);
    }

    @Test
    void createAndDeleteTask() {
        Task createdTask = taskService.create(
                TestFixtures.getTasks().getFirst()
        );

        when()
                .get("/api/tasks/{id}", createdTask.getId())
                .then()
                .statusCode(200);

        when()
                .delete("/api/tasks/{id}", createdTask.getId())
                .then()
                .statusCode(200);

        when()
                .get("/api/tasks/{id}", createdTask.getId())
                .then()
                .statusCode(400);

    }
    @Test
    void getAllCreatedUsers() {
        List<User> createdUsers = TestFixtures.createUsers(userService);

        List<User> retrievedUsers = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/users")
                .then()
                .statusCode(200)
                .body(".", hasSize(createdUsers.size()))
                .and()
                .extract().jsonPath().getList("$", UserDto.class)
                .stream()
                .map(userDtoMapper::toBusiness)
                .toList();

        assertThat(retrievedUsers)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("createdAt") // prevent issues due to differing timestamps after conversions
                .containsExactlyInAnyOrderElementsOf(createdUsers);
    }

    void createAndGetUserByID() {
        List<User> createdUsers = TestFixtures.getUsers();
        User created = createdUsers.get(0);

        // POST request to create the user
        Integer createdUserId = given()
                .contentType(ContentType.JSON)
                .body(created)
                .when()
                .post("/api/users")
                .then()
                .statusCode(200)
                .extract()
                .path("id"); // Extract the ID from the response

        // Verify user can be retrieved by ID
        User retrievedUser = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/users/{id}", createdUserId) // Use extracted ID
                .then()
                .statusCode(200)
                .extract()
                .as(User.class);

        // Assert the retrieved user matches the created user
        assertThat(retrievedUser)
                .usingRecursiveComparison()
                .isEqualTo(created);
    }

    //TODO: Add at least one test for each new endpoint in the users controller (the create endpoint can be tested as part of the other endpoints).
}