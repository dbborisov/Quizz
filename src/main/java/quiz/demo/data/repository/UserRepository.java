package quiz.demo.data.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import quiz.demo.data.model.Role;
import quiz.demo.data.model.User;

import java.util.List;

@Repository()
public interface UserRepository extends JpaRepository<User, Long> {
	User findByEmail(String email);

	User findByUsername(String username);
	User findUserByRole(Role role);
	List<User> findAllByRoleNotLike(Role role);
}
