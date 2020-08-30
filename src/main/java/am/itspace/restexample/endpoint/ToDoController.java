package am.itspace.restexample.endpoint;


import am.itspace.restexample.dto.ToDo;
import am.itspace.restexample.exception.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class ToDoController {

    String baseUrl = "https://jsonplaceholder.typicode.com/todos";

    @GetMapping("/todos")
    public List<ToDo> toDos() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ToDo[]> response = restTemplate.getForEntity(baseUrl, ToDo[].class);
        ToDo[] body = response.getBody();
        if (body == null) {
            return new ArrayList<>();
        }
        return Arrays.asList(body);
    }

    @GetMapping("todos/{id}")
    public ToDo toDo(@PathVariable("id") int id) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ToDo> todo = restTemplate.getForEntity(baseUrl + "/" + id, ToDo.class);
        if (todo.getBody() != null) {
            return todo.getBody();
        }
        throw new ResourceNotFoundException();
    }
}
