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
    private String username;

    @Column(name = "score",nullable = false)
    private long score;





}
