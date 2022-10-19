package com.challenge.tenpo.service.extern;

import com.challenge.tenpo.exception.PercentageNotFoundException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ServiceExternoMockImpl implements ServiceExternoMock {

    @Value("${externo.rest.client.baseUrl}")
    private String baseUrl;
    @Autowired
    private final RestTemplate restTemplate;

    private Integer lastValue = null;

    @Override
    @CircuitBreaker(name = "ServiceExterno", fallbackMethod = "percentageDefault")
    @Retry(name = "ServiceExterno", fallbackMethod = "percentageDefault")
    @RateLimiter(name = "ServiceExterno", fallbackMethod = "percentageDefault")
    public int getValuePercentage() {

        Object response = restTemplate.getForObject(baseUrl, Object.class);
        List<Object> result =
                Optional.ofNullable((List<Object>) response)
                        .orElseThrow(() -> new PercentageNotFoundException("Percentage not found"));
        if (!result.isEmpty()) {
            int searchValue = (int) ((Map<?, ?>) result.get(0)).get("random");
            lastValue = Integer.valueOf(searchValue);
            return lastValue;
        }
        throw new PercentageNotFoundException("Percentage not found");
    }

    public int percentageDefault(Exception e) {
        if (lastValue!=null) return lastValue;
        throw new PercentageNotFoundException("Percentage not found");
    }
}
