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
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticService {

    private final StatisticRepository statisticRepository;

    public byte[] generateExcelStatistic(int formId) throws IOException {
        StatisticDTO statisticDTO = getFormStatistic(formId);

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Statistic #" + formId);

            Font font = workbook.createFont();
            font.setFontName("Tahoma");
            font.setFontHeightInPoints((short) 18);

            Font boldFont = workbook.createFont();
            boldFont.setFontName("Tahoma");
            boldFont.setFontHeightInPoints((short) 18);
            boldFont.setBold(true);

            CellStyle answerStyle = workbook.createCellStyle();
            answerStyle.setFont(font);
            answerStyle.setAlignment(HorizontalAlignment.CENTER);
            answerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            CellStyle questionStyle = workbook.createCellStyle();
            questionStyle.setFont(boldFont);
            questionStyle.setAlignment(HorizontalAlignment.CENTER);
            questionStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            int rowNumber = 0;

            Row numberOfCompletionsTitleRow = sheet.createRow(rowNumber++);
            Cell numberOfCompletionTitleCell = numberOfCompletionsTitleRow.createCell(0);
            numberOfCompletionTitleCell.setCellValue("Количество прошедших опрос: " + statisticDTO.getNumberOfCompletions());
            numberOfCompletionTitleCell.setCellStyle(questionStyle);

            for (QuestionStatisticDTO questionStatistic : statisticDTO.getQuestionStatistic()) {
                rowNumber++;

                Row questionRow = sheet.createRow(rowNumber++);
                Cell questionCell = questionRow.createCell(0);
                questionCell.setCellValue(questionStatistic.getQuestionText());
                questionCell.setCellStyle(questionStyle);

                Object statistic = questionStatistic.getStatistic();

                if (statistic instanceof List) {
                    List<String> textAnswers = (List<String>) statistic;
                    Row answerRow = sheet.createRow(rowNumber++);
                    Cell titleCell = answerRow.createCell(0);
                    titleCell.setCellValue("All answers:");
                    titleCell.setCellStyle(answerStyle);
                    for (int i = 0; i < textAnswers.size(); i++) {
                        Cell cell = answerRow.createCell(i + 1);
                        cell.setCellValue(textAnswers.get(i));
                        cell.setCellStyle(answerStyle);
                    }
                } else if (statistic instanceof NumericStatisticDTO) {
                    NumericStatisticDTO numericStatistic = (NumericStatisticDTO) statistic;

                    Row statsHeaderRow = sheet.createRow(rowNumber++);
                    statsHeaderRow.createCell(0).setCellValue("Min");
                    statsHeaderRow.createCell(1).setCellValue("Max");
                    statsHeaderRow.createCell(2).setCellValue("Avg");
                    for (int i = 0; i < 3; i++) {
                        statsHeaderRow.getCell(i).setCellStyle(answerStyle);
                    }

                    Row statsValuesRow = sheet.createRow(rowNumber++);
                    statsValuesRow.createCell(0).setCellValue(numericStatistic.getMinAnswer());
                    statsValuesRow.createCell(1).setCellValue(numericStatistic.getMaxAnswer());
                    statsValuesRow.createCell(2).setCellValue(numericStatistic.getAvgAnswer());
                    for (int i = 0; i < 3; i++) {
                        statsValuesRow.getCell(i).setCellStyle(answerStyle);
                    }

                    Row allAnswersRow = sheet.createRow(rowNumber++);
                    Cell titleCell = allAnswersRow.createCell(0);
                    titleCell.setCellValue("All answers:");
                    titleCell.setCellStyle(answerStyle);
                    for (int i = 0; i < numericStatistic.getAnswers().size(); i++) {
                        Cell cell = allAnswersRow.createCell(i + 1);
                        cell.setCellValue(numericStatistic.getAnswers().get(i));
                        cell.setCellStyle(answerStyle);
                    }
                } else if (statistic instanceof ChoicesStatisticDTO) {
                    ChoicesStatisticDTO choicesStatistic = (ChoicesStatisticDTO) statistic;

                    Row answersRow = sheet.createRow(rowNumber++);
                    for (int i = 0; i < choicesStatistic.getAnswers().size(); i++) {
                        Cell cell = answersRow.createCell(i);
                        cell.setCellValue(choicesStatistic.getAnswers().get(i));
                        cell.setCellStyle(answerStyle);
                    }

                    Row countsRow = sheet.createRow(rowNumber++);
                    for (int i = 0; i < choicesStatistic.getNumberOfAnswered().size(); i++) {
                        Cell cell = countsRow.createCell(i);
                        cell.setCellValue(choicesStatistic.getNumberOfAnswered().get(i));
                        cell.setCellStyle(answerStyle);
                    }

                    Row percentagesRow = sheet.createRow(rowNumber++);
                    for (int i = 0; i < choicesStatistic.getPercentageOfAnswered().size(); i++) {
                        Cell cell = percentagesRow.createCell(i);
                        cell.setCellValue(choicesStatistic.getPercentageOfAnswered().get(i) + "%");
                        cell.setCellStyle(answerStyle);
                    }
                }
            }

            for (int i = 0; i < sheet.getRow(0).getLastCellNum(); i++) {
                sheet.autoSizeColumn(i);
            }

            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                workbook.write(baos);
                return baos.toByteArray();
            }
        }
    }

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