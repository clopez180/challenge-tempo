package com.challenge.tenpo.unit.controller;

import com.challenge.tenpo.controller.ChallengeController;
import com.challenge.tenpo.dto.AddResponseDTO;
import com.challenge.tenpo.dto.HistoriesResponseDTO;
import com.challenge.tenpo.dto.HistoryDTO;
import com.challenge.tenpo.service.IChallengeService;
import com.challenge.tenpo.unit.BaseTest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ChallengeControllerUnitTest extends BaseTest {

    @Mock
    IChallengeService challengeService;

    @InjectMocks
    ChallengeController challengeController;

    @Test
    void givenNumbers_whenRandomPercentage_Ok() {

        when(challengeService.getAdd(any(BigDecimal.class), any(BigDecimal.class)))
                .thenReturn(new BigDecimal(12));

        ResponseEntity<AddResponseDTO> response =
                challengeController.getSum(
                        "token", new BigDecimal(5), new BigDecimal(5));
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getResult());
        assertEquals(response.getBody().getResult().intValue(), 12);
        verify(challengeService, times(1))
                .getAdd(any(BigDecimal.class), any(BigDecimal.class));
    }

    public static HistoryDTO getDefaultHistoryDto() {
        return HistoryDTO.builder()
                .endpoint("endpoint")
                .status("status")
                .build();
    }

    public static List<HistoryDTO> getDefaultListHistoryDto() {
        return List.of(getDefaultHistoryDto());
    }

    public static HistoriesResponseDTO getDefaultResult() {

        List<HistoryDTO> history = new ArrayList<>();
        history.add(HistoryDTO.builder()
                .endpoint("endpoint")
                .status("status")
                .build());
        return HistoriesResponseDTO.builder().history(history).build();
    }


    @Test
    void givenRequest_whenFindAll_thenContent() {

        List<HistoryDTO> histories = getDefaultListHistoryDto();
        HistoriesResponseDTO historiesResponse =getDefaultResult();

        when(challengeService.getHistories(anyInt(), anyInt(), anyString()))
                .thenReturn(historiesResponse);

        ResponseEntity<HistoriesResponseDTO> response =
                challengeController.getHistories(
                        "token", 1, 10, "id");
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertNotNull(response.getBody());
        assertEquals(response.getBody().getHistory().size(), histories.size());
        verify(challengeService, times(1)).getHistories(anyInt(), anyInt(), anyString());
    }


}
