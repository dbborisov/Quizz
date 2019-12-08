package quiz.demo.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import quiz.demo.data.model.Log;

import java.util.Optional;

@Repository
public interface LogRepository extends JpaRepository<Log, String> {

    Optional<Log> findLogByUsername(String username);

}
