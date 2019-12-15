package quiz.demo.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import quiz.demo.data.model.Scores;

import java.util.List;

@Repository
public interface ScoreRepository extends JpaRepository< Scores,Long> {

    List<Scores> findAllByUserId(long id);
    List<Scores> findAllByQuizId(long id);

}
