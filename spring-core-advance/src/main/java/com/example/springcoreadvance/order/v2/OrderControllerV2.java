package com.example.springcoreadvance.order.v2;

import com.example.springcoreadvance.trace.TraceStatus;
import com.example.springcoreadvance.trace.v2.TraceV2;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderControllerV2 {

  private final OrderServiceV2 orderService;
  private final TraceV2 trace;

  @GetMapping("/v2/request")
  public String request(String itemId) {

    TraceStatus status = null;
    try {
      status = trace.begin("OrderControllerV2.request");
      orderService.orderItem(status.getTraceId(), itemId);
      trace.end(status);
      return "ok";
    } catch (Exception e) {
      trace.exception(status, e);
      throw e;
    }
  }
}

/*
애플리케이션 로직에 침투했을 뿐만 아니라 parameter 로
현재 상태를 전달해야 하기 때문에 method signature 가 변경되어야 한다
만약 인터페이스를 구현해놨었다면 인터페이스의 메서드 시그니처까지 변경되어야 한다
 */