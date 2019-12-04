package rest.demo.service.accesscontrol;


import org.springframework.stereotype.Service;
import rest.demo.data.model.AuthenticatedUser;
import rest.demo.data.model.Quiz;
import rest.demo.exceptions.UnauthorizedActionException;

@Service("AccessControlQuiz")
public class AccessControlServiceQuiz extends AccessControlServiceUserOwned<Quiz> {

	/*
	 * As long as the user is authenticated, it can create a Quiz.
	 */
	@Override
	public void canUserCreateObject(AuthenticatedUser user, Quiz object) throws UnauthorizedActionException {
		if (user == null) {
			throw new UnauthorizedActionException();
		}
	}

}
