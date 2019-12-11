package quiz.demo.data.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import quiz.demo.data.model.Question;
import quiz.demo.data.model.Quiz;

import java.util.List;

@Repository()
public interface QuestionRepository extends JpaRepository<Question, Long> {
	int countByQuiz(Quiz quiz);

	int countByQuizAndIsValidTrue(Quiz quiz);

	List<Question> findByQuizOrderByOrderAsc(Quiz quiz);

	List<Question> findByQuizAndIsValidTrueOrderByOrderAsc(Quiz quiz);

}
