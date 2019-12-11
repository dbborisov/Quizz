package quiz.demo.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import quiz.demo.data.model.Answer;
import quiz.demo.data.model.Question;

import java.util.List;

@Repository()
public interface AnswerRepository extends JpaRepository<Answer, Long> {

	int countByQuestion(Question question);

	List<Answer> findByQuestionOrderByOrderAsc(Question question);

//	Answer findOne(long id);
}
