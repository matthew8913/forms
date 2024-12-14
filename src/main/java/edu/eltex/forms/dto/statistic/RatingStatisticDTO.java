package edu.eltex.forms.dto.statistic;

import edu.eltex.forms.entities.Answer;
import edu.eltex.forms.entities.Option;
import lombok.Builder;
import lombok.Data;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Builder
public class RatingStatisticDTO {
    private int position;
    private String answerText;
    private double avgPosition;

    public static List<RatingStatisticDTO> getFullRatingStatistic(List<Option> questionOptions, List<Answer> questionAnswers) {
        Map<String, Integer> optionPositionSum = new HashMap<>();

        for (Option option : questionOptions) {
            optionPositionSum.put(option.getText(), 0);
        }

        for (Answer answer : questionAnswers) {
            List<Option> selectedOptions = answer.getSelectedOptions();
            for (int i = 0; i < selectedOptions.size(); i++) {
                Option option = selectedOptions.get(i);
                optionPositionSum.put(option.getText(), optionPositionSum.get(option.getText()) + (i + 1));
            }
        }

        List<RatingStatisticDTO> ratingStatistics = questionOptions.stream()
                .map(option -> {
                    int sumPositions = optionPositionSum.get(option.getText());
                    return RatingStatisticDTO.builder()
                            .answerText(option.getText())
                            .avgPosition(sumPositions == 0 ? 0 : sumPositions / (double) questionAnswers.size())
                            .build();
                })
                .sorted(Comparator.comparingDouble(RatingStatisticDTO::getAvgPosition))
                .collect(Collectors.toList());

        for (int i = 0; i < ratingStatistics.size(); i++) {
            ratingStatistics.get(i).setPosition(i + 1);
        }

        return ratingStatistics;
    }
}
