package quiz.demo.service.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ScoreServiceModel extends BaseServiceModel{

    private String username;
    private Long userId;

    private String quizName;
    private Long quizId;

    private double score;
}
