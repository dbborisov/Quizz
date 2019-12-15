package quiz.demo.service.accesscontrol.aspects;


import org.aspectj.lang.ProceedingJoinPoint;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import quiz.demo.data.model.Question;
import quiz.demo.service.accesscontrol.AccessControlService;
import quiz.demo.service.accesscontrol.AccessControlServiceQuestion;

import static org.mockito.Mockito.*;

public class AccessControlAspectsQuestionTests {
	private static final Long ID = 1l;

	// Class under test
	AccessControlAspectsQuestion aspect;

	// Mocks
	AccessControlService<Question> accessControlService;
	ProceedingJoinPoint proceedingJoinPoint;

	Question question = new Question();

	@BeforeEach
	public void before() {
		accessControlService = mock(AccessControlServiceQuestion.class);
		aspect = new AccessControlAspectsQuestion();
		aspect.setAccessControlService(accessControlService);

		proceedingJoinPoint = mock(ProceedingJoinPoint.class);
	}

	@Test
	public void create_shouldForward() throws Throwable {
		question.setId(null);

		aspect.save(proceedingJoinPoint, question);

		verify(accessControlService, times(1)).canCurrentUserCreateObject(question);
		verify(accessControlService, never()).canCurrentUserUpdateObject(question);
	}

	@Test
	public void read_shouldForward() throws Throwable {
		aspect.find(proceedingJoinPoint, ID);

		verify(accessControlService, times(1)).canCurrentUserReadObject(ID);
	}

	@Test
	public void readAll_shouldForward() throws Throwable {
		aspect.findAll(proceedingJoinPoint);

		verify(accessControlService, times(1)).canCurrentUserReadAllObjects();
	}

	@Test
	public void update_shouldForward() throws Throwable {
		question.setId(ID);

		aspect.save(proceedingJoinPoint, question);

		verify(accessControlService, never()).canCurrentUserCreateObject(question);
		verify(accessControlService, times(1)).canCurrentUserUpdateObject(question);
	}

	@Test
	public void delete_shouldForward() throws Throwable {
		aspect.delete(proceedingJoinPoint, question);

		verify(accessControlService, times(1)).canCurrentUserDeleteObject(question);
	}

}
