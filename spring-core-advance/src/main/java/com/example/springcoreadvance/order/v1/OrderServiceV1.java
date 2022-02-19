package com.example.springcoreadvance.order.v1;

import com.example.springcoreadvance.trace.TraceStatus;
import com.example.springcoreadvance.trace.v0.TraceV0;
import org.springframework.stereotype.Service;

@Service
public record OrderServiceV1(OrderRepositoryV1 orderRepository, TraceV0 trace) {

  public void orderItem(String itemId) {
    TraceStatus status = null;
    try {
      status = trace.begin("OrderServiceV1.orderItem");
      orderRepository.save(itemId);
      trace.end(status);
    } catch (Exception e) {
      trace.exception(status, e);
      throw e;
    }
  }
}
