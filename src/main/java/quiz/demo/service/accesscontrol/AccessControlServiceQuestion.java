package quiz.demo.service.accesscontrol;


import org.springframework.stereotype.Service;
import quiz.demo.data.model.Question;

@Service
public class AccessControlServiceQuestion extends AccessControlServiceUserOwned<Question> {

}
