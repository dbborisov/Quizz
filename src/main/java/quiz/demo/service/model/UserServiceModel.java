package quiz.demo.service.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import quiz.demo.data.model.Role;

import java.util.Calendar;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class UserServiceModel {
    private long id;
    private String email;
    private String username;
    private String password;
    private boolean enabled;
    private Date createdDate;
    private String role;
}
