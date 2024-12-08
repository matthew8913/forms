package edu.eltex.forms.service;

import edu.eltex.forms.entities.Option;
import edu.eltex.forms.entities.Question;
import edu.eltex.forms.mapper.OptionMapper;
import edu.eltex.forms.repository.OptionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OptionService {

    private final OptionRepository optionRepository;
    private final OptionMapper optionMapper;

    public List<Option> convertIntoExistingOptions(List<Option> options, Question question) {
        return options.stream()
                .map(option -> optionRepository.findByTextAndQuestion(option.getText(), question))
                .toList();
    }
}
