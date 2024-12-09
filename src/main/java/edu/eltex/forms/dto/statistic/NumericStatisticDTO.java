package edu.eltex.forms.dto.statistic;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class NumericStatisticDTO {
    private List<Integer> answers;
    private Integer minAnswer;
    private Integer maxAnswer;
    private Integer avgAnswer;

    public static NumericStatisticDTO getFullNumericStatistic(List<Integer> answers) {
        Integer minAnswer = answers.stream()
                .min(Integer::compare)
                .orElse(null);

        Integer maxAnswer = answers.stream()
                .max(Integer::compare)
                .orElse(null);

        Integer avgAnswer = (int) answers.stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);

        return NumericStatisticDTO.builder()
                .answers(answers)
                .minAnswer(minAnswer)
                .maxAnswer(maxAnswer)
                .avgAnswer(avgAnswer)
                .build();
    }

    public Integer getAvgAnswer() {
        return avgAnswer;
    }

    public Integer getMaxAnswer() {
        return maxAnswer;
    }

    public Integer getMinAnswer() {
        return minAnswer;
    }

    public List<Integer> getAnswers() {
        return answers;
    }
}