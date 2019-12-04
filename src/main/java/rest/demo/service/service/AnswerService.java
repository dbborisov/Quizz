package rest.demo.service.service;

import rest.demo.data.model.Answer;
import rest.demo.data.model.Question;
import rest.demo.exceptions.ResourceUnavailableException;
import rest.demo.exceptions.UnauthorizedActionException;

import java.util.List;

public interface AnswerService {
    Answer save(Answer answer) throws UnauthorizedActionException;

    Answer find(Long id) throws ResourceUnavailableException;

    Answer update(Answer newAnswer) throws UnauthorizedActionException, ResourceUnavailableException;

    void delete(Answer answer) throws UnauthorizedActionException, ResourceUnavailableException;

    List<Answer> findAnswersByQuestion(Question question);

    int countAnswersInQuestion(Question question);
}
