package am.itspace.restexample.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ToDo {

    private int userId;
    private int id;
    private String title;
    private boolean completed;
}
