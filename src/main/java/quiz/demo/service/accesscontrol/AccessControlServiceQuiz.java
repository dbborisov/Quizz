package quiz.demo.service.accesscontrol;


import org.springframework.stereotype.Service;
import quiz.demo.data.model.AuthenticatedUser;
import quiz.demo.data.model.Quiz;
import quiz.demo.exceptions.UnauthorizedActionException;

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
