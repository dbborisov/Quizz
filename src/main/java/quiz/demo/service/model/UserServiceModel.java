package quiz.demo.service.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Calendar;

@Getter
@Setter
@NoArgsConstructor
public class UserServiceModel {
    private long id;
    private String email;
    private String username;
    private String password;
    private boolean enabled;
    private Calendar createdDate;
}
