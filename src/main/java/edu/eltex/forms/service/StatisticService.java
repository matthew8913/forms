package edu.eltex.forms.service;

import edu.eltex.forms.dto.statistic.ChoicesStatisticDTO;
import edu.eltex.forms.dto.statistic.NumericStatisticDTO;
import edu.eltex.forms.dto.statistic.QuestionStatisticDTO;
import edu.eltex.forms.dto.statistic.StatisticDTO;
import edu.eltex.forms.entities.Answer;
import edu.eltex.forms.entities.Option;
import edu.eltex.forms.entities.Question;
import edu.eltex.forms.enums.QuestionType;
import edu.eltex.forms.repository.StatisticRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticService {

    private final StatisticRepository statisticRepository;

    public StatisticDTO getFormStatistic(int formId) {
        Integer numberOfCompletions = statisticRepository.countNumberOfCompletions(formId);

        List<Question> allQuestions = statisticRepository.getAllQuestions(formId);
        List<Answer> allAnswers = statisticRepository.getAllAnswers(formId);
        List<Option> allOptions = statisticRepository.getAllOptions(formId);

        Map<Integer, List<Answer>> answersByQuestionId = allAnswers.stream()
                .collect(Collectors.groupingBy(answer -> answer.getQuestion().getId()));

        Map<Integer, List<Option>> optionsByQuestionId = allOptions.stream()
                .collect(Collectors.groupingBy(option -> option.getQuestion().getId()));

        List<QuestionStatisticDTO> allQuestionStatistics = allQuestions.stream()
                .map(question -> {
                    List<Answer> questionAnswers = answersByQuestionId.getOrDefault(question.getId(), List.of());
                    List<Option> questionOptions = optionsByQuestionId.getOrDefault(question.getId(), List.of());

                    return switch (question.getType()) {
                        case TEXT -> getTextQuestionStatistic(question, questionAnswers);
                        case NUMERIC -> getNumericQuestionStatistic(question, questionAnswers);
                        case SINGLE_CHOICE, MULTIPLE_CHOICE ->
                                getChoicesQuestionStatistic(question, questionAnswers, questionOptions, numberOfCompletions);
                        case RATING -> getRatingStatistic(question, questionAnswers, questionOptions);
                    };
                })
                .collect(Collectors.toList());

        return StatisticDTO.builder()
                .numberOfCompletions(numberOfCompletions)
                .questionStatistic(allQuestionStatistics)
                .build();
    }

    private QuestionStatisticDTO getRatingStatistic(Question question, List<Answer> questionAnswers, List<Option> questionOptions) {
        Map<String, Integer> optionScores = questionOptions.stream()
                .collect(Collectors.toMap(Option::getText, option -> 0));

        for (Answer answer : questionAnswers) {
            List<Option> selectedOptions = answer.getSelectedOptions();
            for (int i = 0; i < selectedOptions.size(); i++) {
                Option option = selectedOptions.get(i);
                optionScores.put(option.getText(), optionScores.get(option.getText()) + (selectedOptions.size() - i));
            }
        }

        Map<Integer, List<String>> optionsByScore = optionScores.entrySet().stream()
                .collect(Collectors.groupingBy(
                        Map.Entry::getValue,
                        Collectors.mapping(Map.Entry::getKey, Collectors.toList())
                ));

        List<Integer> sortedScores = optionsByScore.keySet().stream()
                .sorted(Comparator.reverseOrder())
                .toList();

        Map<Integer, List<String>> orderedOptionsByScore = new LinkedHashMap<>();
        for (int i = 0; i < sortedScores.size(); i++) {
            orderedOptionsByScore.put(i + 1, optionsByScore.get(sortedScores.get(i)));
        }

        return QuestionStatisticDTO.builder()
                .questionType(QuestionType.RATING)
                .questionText(question.getText())
                .statistic(orderedOptionsByScore)
                .build();
    }
    public QuestionStatisticDTO getTextQuestionStatistic(Question question, List<Answer> answers) {
        List<String> textAnswers = answers.stream()
                .map(Answer::getAnswerText)
                .collect(Collectors.toList());

        return QuestionStatisticDTO.builder()
                .questionType(QuestionType.TEXT)
                .questionText(question.getText())
                .statistic(textAnswers)
                .build();
    }

    public QuestionStatisticDTO getNumericQuestionStatistic(Question question, List<Answer> answers) {
        List<Integer> numericAnswers = answers.stream()
                .map(answer -> Integer.parseInt(answer.getAnswerText()))
                .collect(Collectors.toList());

        NumericStatisticDTO numericStatistic = NumericStatisticDTO.getFullNumericStatistic(numericAnswers);

        return QuestionStatisticDTO.builder()
                .questionType(QuestionType.NUMERIC)
                .questionText(question.getText())
                .statistic(numericStatistic)
                .build();
    }

    public QuestionStatisticDTO getChoicesQuestionStatistic(Question question, List<Answer> answers, List<Option> options, int numberOfCompletions) {
        List<String> answeredAnswers = answers.stream()
                .flatMap(answer -> answer.getSelectedOptions().stream())
                .map(Option::getText)
                .collect(Collectors.toList());

        List<String> allPossibleAnswers = options.stream()
                .map(Option::getText)
                .collect(Collectors.toList());

        ChoicesStatisticDTO choicesStatistic = ChoicesStatisticDTO.getFullChoisesStatisticDTO(answeredAnswers, allPossibleAnswers, numberOfCompletions);

        return QuestionStatisticDTO.builder()
                .questionType(question.getType() == QuestionType.SINGLE_CHOICE ? QuestionType.SINGLE_CHOICE : QuestionType.MULTIPLE_CHOICE) // Добавляем тип вопроса
                .questionText(question.getText())
                .statistic(choicesStatistic)
                .build();
    }
}