package edu.eltex.forms.controller;

import edu.eltex.forms.dto.statistic.StatisticDTO;
import edu.eltex.forms.service.StatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/statistic")
@RequiredArgsConstructor
@Validated
public class StatisticController {

    private final StatisticService statisticService;

    @PreAuthorize("hasRole('CREATOR')")
    @GetMapping("/{formId}")
    public ResponseEntity<StatisticDTO> getNumberOfCompletions(@PathVariable Integer formId) {
        StatisticDTO completions = statisticService.getFormStatistic(formId);
        return ResponseEntity.ok(completions);
    }
}