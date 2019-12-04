package quiz.demo.service.accesscontrol.aspects;


import org.aspectj.lang.ProceedingJoinPoint;
import quiz.demo.data.model.BaseEntity;

public interface AccessControlAspects<T extends BaseEntity> {
	Object save(ProceedingJoinPoint proceedingJoinPoint, T object) throws Throwable;

	Object find(ProceedingJoinPoint proceedingJoinPoint, Long id) throws Throwable;

	Object findAll(ProceedingJoinPoint proceedingJoinPoint) throws Throwable;

	Object delete(ProceedingJoinPoint proceedingJoinPoint, T object) throws Throwable;
}