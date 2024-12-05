package edu.eltex.forms.service;

import edu.eltex.forms.dto.statistic.ChoisesStatisticDTO;
import edu.eltex.forms.dto.statistic.NumericStatisticDTO;
import edu.eltex.forms.dto.statistic.QuestionStatisticDTO;
import edu.eltex.forms.dto.statistic.StatisticDTO;
import edu.eltex.forms.entities.Answer;
import edu.eltex.forms.entities.Option;
import edu.eltex.forms.entities.Question;
import edu.eltex.forms.repository.StatisticRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class StatisticService {

    private final StatisticRepository statisticRepository;

    private List<QuestionStatisticDTO> getChoicesQuestionStatistics(int formId, int numberOfCompletions) {
        List<Answer> choicesAnswers = statisticRepository.getChoicesAnswers(formId);
        List<Option> allOptions = statisticRepository.getAllOptions(formId);

        Map<String, List<String>> questionAnswersMap = choicesAnswers.stream()
                .collect(Collectors.groupingBy(
                        answer -> answer.getQuestion().getText(),
                        Collectors.flatMapping(answer -> answer.getSelectedOptions().stream().map(Option::getText), Collectors.toList())
                ));

        Map<String, List<String>> allOptionsMap = allOptions.stream()
                .collect(Collectors.groupingBy(
                        option -> option.getQuestion().getText(),
                        Collectors.mapping(Option::getText, Collectors.toList())
                ));

        return allOptionsMap.entrySet().stream()
                .map(entry -> {
                    List<String> answeredAnswers = questionAnswersMap.getOrDefault(entry.getKey(), new ArrayList<>());
                    List<String> allPossibleAnswers = entry.getValue();

                    ChoisesStatisticDTO choicesStatistic = ChoisesStatisticDTO.getFullChoisesStatisticDTO(answeredAnswers, allPossibleAnswers, numberOfCompletions);

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

    private List<QuestionStatisticDTO> getTextQuestionStatistics(int formId) {
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
        Integer numberOfCompletions = statisticRepository.countNumberOfCompletions(formId);

        List<QuestionStatisticDTO> numericQuestionStatistics = getNumericQuestionStatistics(formId);
        List<QuestionStatisticDTO> textQuestionStatistics = getTextQuestionStatistics(formId);
        List<QuestionStatisticDTO> choicesQuestionStatistics = getChoicesQuestionStatistics(formId, numberOfCompletions);

        List<QuestionStatisticDTO> allQuestionStatistics = Stream.concat(
                Stream.concat(textQuestionStatistics.stream(), numericQuestionStatistics.stream()),
                choicesQuestionStatistics.stream()
        ).collect(Collectors.toList());

        List<Question> allQuestions = statisticRepository.getAllQuestions(formId);

        Map<String, Integer> questionIdMap = allQuestions.stream()
                .collect(Collectors.toMap(Question::getText, Question::getId));

        allQuestionStatistics.sort(Comparator.comparingInt(statistic -> questionIdMap.get(statistic.getQuestionText())));

        return StatisticDTO.builder()
                .numberOfCompletions(numberOfCompletions)
                .questionStatistic(allQuestionStatistics)
                .build();
    }
}