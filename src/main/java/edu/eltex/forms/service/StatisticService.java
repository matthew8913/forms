package edu.eltex.forms.service;

import edu.eltex.forms.dto.statistic.ChoicesStatisticDTO;
import edu.eltex.forms.dto.statistic.NumericStatisticDTO;
import edu.eltex.forms.dto.statistic.QuestionStatisticDTO;
import edu.eltex.forms.dto.statistic.StatisticDTO;
import edu.eltex.forms.entities.Answer;
import edu.eltex.forms.entities.Option;
import edu.eltex.forms.entities.Question;
import edu.eltex.forms.repository.StatisticRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
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

                    QuestionStatisticDTO questionStatisticDTO = null;

                    switch (question.getType()) {
                        case TEXT:
                            questionStatisticDTO = getTextQuestionStatistic(question, questionAnswers);
                            break;
                        case NUMERIC:
                            questionStatisticDTO = getNumericQuestionStatistic(question, questionAnswers);
                            break;
                        case SINGLE_CHOICE:
                        case MULTIPLE_CHOICE:
                            questionStatisticDTO = getChoicesQuestionStatistic(question, questionAnswers, questionOptions, numberOfCompletions);
                            break;
                    }

                    return questionStatisticDTO;
                })
                .collect(Collectors.toList());

        return StatisticDTO.builder()
                .numberOfCompletions(numberOfCompletions)
                .questionStatistic(allQuestionStatistics)
                .build();
    }

    public QuestionStatisticDTO getTextQuestionStatistic(Question question, List<Answer> answers) {
        List<String> textAnswers = answers.stream()
                .map(Answer::getAnswerText)
                .collect(Collectors.toList());

        return QuestionStatisticDTO.builder()
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
                .questionText(question.getText())
                .statistic(choicesStatistic)
                .build();
    }
}