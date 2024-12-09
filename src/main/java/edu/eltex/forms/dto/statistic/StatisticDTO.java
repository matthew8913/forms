package edu.eltex.forms.dto.statistic;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class StatisticDTO {
    private Integer numberOfCompletions;
    private List<QuestionStatisticDTO> questionStatistic;

    public Integer getNumberOfCompletions() {
        return numberOfCompletions;
    }

    public List<QuestionStatisticDTO> getQuestionStatistic() {
        return questionStatistic;
    }
}
