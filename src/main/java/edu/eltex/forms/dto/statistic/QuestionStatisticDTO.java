package edu.eltex.forms.dto.statistic;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuestionStatisticDTO {
    private String questionText;
    private Object statistic;
}