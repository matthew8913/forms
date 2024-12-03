package edu.eltex.forms.service;

import edu.eltex.forms.dto.statistic.ChoisesStatisticDTO;
import edu.eltex.forms.dto.statistic.NumericStatisticDTO;
import edu.eltex.forms.dto.statistic.QuestionStatisticDTO;
import edu.eltex.forms.dto.statistic.StatisticDTO;
import edu.eltex.forms.entities.Answer;
import edu.eltex.forms.repository.StatisticRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class StatisticService {

    private final StatisticRepository statisticRepository;

    private List<QuestionStatisticDTO> getChoicesQuestionStatistics(int formId, int numberOfResponses) {
        List<Answer> choicesAnswers = statisticRepository.getChoisesAnswers(formId);

        Map<String, List<String>> questionAnswersMap = choicesAnswers.stream()
                .collect(Collectors.groupingBy(
                        answer -> answer.getQuestion().getText(),
                        Collectors.mapping(answer -> answer.getSelectedOption().getText(), Collectors.toList())
                ));

        return questionAnswersMap.entrySet().stream()
                .map(entry -> {
                    ChoisesStatisticDTO choicesStatistic = ChoisesStatisticDTO.getFullChoisesStatisticDTO(entry.getValue(), numberOfResponses);
                    return QuestionStatisticDTO.builder()
                            .questionText(entry.getKey())
                            .statistic(choicesStatistic)
                            .build();
                })
                .collect(Collectors.toList());
    }

    private List<QuestionStatisticDTO> getNumericQuestionStatistics(int formId) {
        List<Answer> numericAnswers = statisticRepository.getNumericAnswers(formId);

        Map<String, List<Integer>> questionAnswersMap = numericAnswers.stream()
                .collect(Collectors.groupingBy(
                        answer -> answer.getQuestion().getText(),
                        Collectors.mapping(answer -> Integer.parseInt(answer.getAnswerText()), Collectors.toList())
                ));

        return questionAnswersMap.entrySet().stream()
                .map(entry -> {
                    NumericStatisticDTO numericStatistic = NumericStatisticDTO.getFullNumericStatistic(entry.getValue());
                    return QuestionStatisticDTO.builder()
                            .questionText(entry.getKey())
                            .statistic(numericStatistic)
                            .build();
                })
                .collect(Collectors.toList());
    }

    private List<QuestionStatisticDTO> getTextQuestionStatistics(int formId){
        List<Answer> textAnswers = statisticRepository.getTextAnswers(formId);

        Map<String, List<String>> textQuestionAnswersMap = textAnswers.stream()
                .collect(Collectors.groupingBy(
                        answer -> answer.getQuestion().getText(),
                        Collectors.mapping(Answer::getAnswerText, Collectors.toList())
                ));

        return textQuestionAnswersMap.entrySet().stream()
                .map(entry -> QuestionStatisticDTO.builder()
                        .questionText(entry.getKey())
                        .statistic(entry.getValue())
                        .build())
                .collect(Collectors.toList());
    }

    public StatisticDTO getFormStatistic(int formId) {
        Integer numberOfResponses = statisticRepository.countNumberOfResponses(formId);

        List<QuestionStatisticDTO> numericQuestionStatistics = getNumericQuestionStatistics(formId);
        List<QuestionStatisticDTO> textQuestionStatistics = getTextQuestionStatistics(formId);
        List<QuestionStatisticDTO> choicesQuestionStatistics = getChoicesQuestionStatistics(formId, numberOfResponses);

        List<QuestionStatisticDTO> allQuestionStatistics = Stream.concat(
                Stream.concat(textQuestionStatistics.stream(), numericQuestionStatistics.stream()),
                choicesQuestionStatistics.stream()
        ).collect(Collectors.toList());

        return StatisticDTO.builder()
                .numberOfResponses(numberOfResponses)
                .questionStatistic(allQuestionStatistics)
                .build();
    }
}