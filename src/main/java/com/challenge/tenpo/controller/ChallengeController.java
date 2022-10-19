package com.challenge.tenpo.controller;

import com.challenge.tenpo.aspect.Track;
import com.challenge.tenpo.dto.HistoriesResponseDTO;
import com.challenge.tenpo.dto.AddResponseDTO;
import com.challenge.tenpo.service.IChallengeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@RestController
@Api("Contract Devices operations")
@RequestMapping("/challenge")
@RequiredArgsConstructor
public class ChallengeController {

    @Autowired
    private final IChallengeService challengeService;

    @Track(endpoint = "/challenge/add")
    @ApiOperation(
            value = "add numbers",
            notes = "Add two given numbers and apply service extern")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            code = 200,
                            message = "Result of add two given numbers with apply service extern"),
            })
    @GetMapping(
            value = "/add",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AddResponseDTO> getSum(
            @RequestHeader(AUTHORIZATION) String token,
            @RequestParam("number_first") BigDecimal numberOne,
            @RequestParam("number_second") BigDecimal numberTwo) {

        log.info("sum with : ({}) and ({})", numberOne, numberTwo);

        BigDecimal result = challengeService.getAdd(numberOne, numberTwo);

        return ResponseEntity.ok(AddResponseDTO.builder()
                .result(result).build());
    }

    @Track(endpoint = "/challenge/history")
    @ApiOperation(
            value = "Histories",
            notes = "Gets histories with pagination")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "histories"),
            })
    @GetMapping(
            value = "/history",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HistoriesResponseDTO> getHistories(
            @RequestHeader(AUTHORIZATION) String token,
            @RequestParam(defaultValue ="0") Integer offset,
            @RequestParam(defaultValue ="100") Integer pageSize ,
            @RequestParam(defaultValue ="id") String sortBy) {
        HistoriesResponseDTO response = challengeService.getHistories(offset, pageSize, sortBy);

        return ResponseEntity.ok(response);
    }

}
