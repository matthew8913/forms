package edu.eltex.forms.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "answers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "completion_id", nullable = false)
    private Completion completion;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    private String answerText;

    @ManyToOne
    @JoinColumn(name = "selected_option_id")
    private Option selectedOption;

    public String getAnswerText() {
        return answerText;
    }

    public Question getQuestion() {
        return question;
    }

    public Option getSelectedOption() {
        return selectedOption;
    }
}
