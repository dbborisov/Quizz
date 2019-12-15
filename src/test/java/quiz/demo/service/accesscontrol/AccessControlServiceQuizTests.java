package quiz.demo.service.accesscontrol;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import quiz.demo.data.model.AuthenticatedUser;
import quiz.demo.data.model.Quiz;
import quiz.demo.data.model.User;

public class AccessControlServiceQuizTests {

	// Service under test
	AccessControlService<Quiz> service;

	User internalUser1 = new User();
	AuthenticatedUser user1 = new AuthenticatedUser(internalUser1);
	User internalUser2 = new User();
	AuthenticatedUser user2 = new AuthenticatedUser(internalUser2);

	Quiz quiz = new Quiz();

	@BeforeEach
	public void before() {
		service = new AccessControlServiceQuiz();

		internalUser1.setId(1l);
		internalUser2.setId(2l);

		quiz.setCreatedBy(user1.getUser());
	}

	@Test
	public void canUserCreateObject_shouldNeverThrowException() {
		service.canUserCreateObject(user1, quiz);
	}

	@Test
	public void canUserReadObject_userOwnsQuiz_shouldAllowRead() {
		service.canUserReadObject(user1, user1.getId());
	}

	@Test
	public void canUserReadObject_userDoentOwnQuiz_shouldAllowRead() {
		service.canUserReadObject(user2, user1.getId());
	}

	@Test
	public void canUserReadAllObjects_shouldNeverThrowException() {
		service.canUserReadAllObjects(user1);
	}

	@Test
	public void canUserUpdateObject_userOwnsQuiz_shouldAllowModification() {
//		service.canUserUpdateObject(user1, quiz);
	}

//	@Test(expected = UnauthorizedActionException.class)
//	public void canUserUpdateObject_userDoesntOwnQuiz_shouldThrowException() {
//		service.canUserUpdateObject(user2, quiz);
//	}
//
//	@Test
//	public void canUserDeleteObject_userOwnsQuiz_shouldAllowModification() {
//		service.canUserDeleteObject(user1, quiz);
//	}
//
//	@Test(expected = UnauthorizedActionException.class)
//	public void canUserDeleteObject_userDoesntOwnQuiz_shouldThrowException() {
//		service.canUserDeleteObject(user2, quiz);
//	}

}