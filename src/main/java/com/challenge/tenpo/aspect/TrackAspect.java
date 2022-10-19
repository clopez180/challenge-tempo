package com.challenge.tenpo.aspect;

import com.challenge.tenpo.dto.HistoryDTO;
import com.challenge.tenpo.service.IHistoryService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class TrackAspect {

    @Autowired
    private final IHistoryService historyService;

    @AfterReturning(pointcut = "@annotation(track)", returning = "responseEntity")
    public void track(
            JoinPoint joinPoint, Track track, ResponseEntity<?> responseEntity) {
        historyService.save(
                HistoryDTO.builder()
                        .endpoint(track.endpoint())
                        .status(responseEntity.getStatusCode().name())
                        .build());
    }
}
