package edu.eltex.forms.service;

import edu.eltex.forms.entities.Option;
import edu.eltex.forms.mapper.OptionMapper;
import edu.eltex.forms.model.OptionModel;
import edu.eltex.forms.repository.OptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class OptionService {

    private final OptionRepository optionRepository;

    @Autowired
    public OptionService(OptionRepository optionRepository) {
        this.optionRepository = optionRepository;
    }

    public OptionModel createOption(OptionModel optionModel) {
        Option option = OptionMapper.INSTANCE.toEntity(optionModel);
        option = optionRepository.save(option);
        return OptionMapper.INSTANCE.toModel(option);
    }

    public List<OptionModel> getAllOptions() {
        return StreamSupport.stream(optionRepository.findAll().spliterator(), false)
                .map(OptionMapper.INSTANCE::toModel)
                .toList();
    }
}
