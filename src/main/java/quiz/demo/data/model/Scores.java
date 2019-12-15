package quiz.demo.data.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@NoArgsConstructor
@Getter
@Setter
@Table
@Entity
public class Scores extends BaseEntity {

    @Column(name = "username",nullable = false)
    private String userName;

    @Column(name = "userId",nullable = false)
    private Long userId;

    @Column(name = "quiz_name",nullable = false)
    private String quizName;

    @Column(name = "quizId",nullable = false)
    private Long quizId;

    @Column(name = "score",nullable = false)
    private double score;





}
