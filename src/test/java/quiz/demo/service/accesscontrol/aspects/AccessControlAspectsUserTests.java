package quiz.demo.service.accesscontrol.aspects;



import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import quiz.demo.data.model.User;
import quiz.demo.service.accesscontrol.AccessControlService;
import quiz.demo.service.accesscontrol.AccessControlServiceUser;

import static org.mockito.Mockito.*;

public class AccessControlAspectsUserTests {
	private static final Long ID = 1l;

	// Class under test
	AccessControlAspectsUser aspect;

	// Mocks
	AccessControlService<User> accessControlService;
	ProceedingJoinPoint proceedingJoinPoint;

	User user = new User();

	@BeforeEach
	public void before() {
		accessControlService = mock(AccessControlServiceUser.class);
		aspect = new AccessControlAspectsUser();
		aspect.setAccessControlService(accessControlService);

		proceedingJoinPoint = mock(ProceedingJoinPoint.class);
	}

	@Test
	public void create_shouldForward() throws Throwable {
		user.setId(null);

		aspect.save(proceedingJoinPoint, user);

		verify(accessControlService, times(1)).canCurrentUserCreateObject(user);
		verify(accessControlService, never()).canCurrentUserUpdateObject(user);
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
		user.setId(ID);

		aspect.save(proceedingJoinPoint, user);

		verify(accessControlService, never()).canCurrentUserCreateObject(user);
		verify(accessControlService, times(1)).canCurrentUserUpdateObject(user);
	}

	@Test
	public void delete_shouldForward() throws Throwable {
		aspect.delete(proceedingJoinPoint, user);

		verify(accessControlService, times(1)).canCurrentUserDeleteObject(user);
	}

}
