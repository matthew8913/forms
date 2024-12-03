package edu.eltex.forms.controller;

import edu.eltex.forms.dto.statistic.StatisticDTO;
import edu.eltex.forms.service.StatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
@Validated
public class StatisticController {

    private final StatisticService statisticService;

    @GetMapping("{form_id}/statistic")
    public ResponseEntity<StatisticDTO> getNumberOfResponses(@PathVariable Integer form_id){
        StatisticDTO response = statisticService.getFormStatistic(form_id);
        return ResponseEntity.ok(response);
    }
}