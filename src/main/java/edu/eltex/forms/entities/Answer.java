package edu.eltex.forms.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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

    @Column(name = "answer_text")
    private String answerText;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "answers_options",
            joinColumns = @JoinColumn(name = "answer_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "option_id", referencedColumnName = "id")
    )
    private List<Option> selectedOptions;

    public Question getQuestion() {
        return question;
    }

    public List<Option> getSelectedOptions() {
        return selectedOptions;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setSelectedOptions(List<Option> selectedOptions) {
        this.selectedOptions = selectedOptions;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}
