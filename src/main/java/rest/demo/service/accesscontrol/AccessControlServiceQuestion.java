package rest.demo.service.accesscontrol;


import org.springframework.stereotype.Service;
import rest.demo.data.model.Question;

@Service
public class AccessControlServiceQuestion extends AccessControlServiceUserOwned<Question> {

}
