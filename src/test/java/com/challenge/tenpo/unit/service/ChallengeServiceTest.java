package com.challenge.tenpo.unit.service;

import com.challenge.tenpo.service.extern.ServiceExternoMock;
import com.challenge.tenpo.service.impl.ChallengeServiceImpl;
import com.challenge.tenpo.unit.BaseTest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class ChallengeServiceTest extends BaseTest {

    @Mock
    ServiceExternoMock serviceExternoMock;

    @InjectMocks
    ChallengeServiceImpl challengeService;

    @Test
    void givenNumbers_whenAdd_thenThrowUserAlreadyExistsException() {

        when(serviceExternoMock.getValuePercentage()).thenReturn(2);

        BigDecimal result =
                challengeService.getAdd(new BigDecimal(7), new BigDecimal(7));

        verify(serviceExternoMock, times(1)).getValuePercentage();
        assertNotNull(result);
        assertEquals(result.intValue(), 14);
    }
}
