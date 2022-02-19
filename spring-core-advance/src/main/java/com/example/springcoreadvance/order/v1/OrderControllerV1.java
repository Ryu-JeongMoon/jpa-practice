package com.example.springcoreadvance.order.v1;

import com.example.springcoreadvance.trace.TraceStatus;
import com.example.springcoreadvance.trace.v0.TraceV0;
import com.example.springcoreadvance.trace.v1.TraceV1;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderControllerV1 {

  private final OrderServiceV1 orderService;
  private final TraceV1 trace;

  @GetMapping("/v1/request")
  public String request(String itemId) {

    TraceStatus status = null;
    try {
      status = trace.begin("OrderControllerV1.request");
      orderService.orderItem(itemId);
      trace.end(status);
      return "ok";
    } catch (Exception e) {
      trace.exception(status, e);
      throw e;
    }
  }
}
