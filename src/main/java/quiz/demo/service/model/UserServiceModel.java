package quiz.demo.service.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import quiz.demo.data.model.Role;

import java.util.Calendar;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class UserServiceModel  {
    private Long id;
    private String email;
    private String username;
    @JsonIgnore
    private String password;
    private boolean enabled;
    private Date createdDate;
    private String role;
}
