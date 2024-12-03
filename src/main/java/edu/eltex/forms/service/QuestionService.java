package edu.eltex.forms.service;

import edu.eltex.forms.entities.Question;
import edu.eltex.forms.mapper.QuestionMapper;
import edu.eltex.forms.model.QuestionModel;
import edu.eltex.forms.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    @Autowired
    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public QuestionModel createQuestion(QuestionModel questionModel) {
        Question question = QuestionMapper.INSTANCE.toEntity(questionModel);
        question = questionRepository.save(question);
        return QuestionMapper.INSTANCE.toModel(question);
    }

    public List<QuestionModel> getAllQuestions() {
        return StreamSupport.stream(questionRepository.findAll().spliterator(), false)
                .map(QuestionMapper.INSTANCE::toModel)
                .toList();
    }
}
