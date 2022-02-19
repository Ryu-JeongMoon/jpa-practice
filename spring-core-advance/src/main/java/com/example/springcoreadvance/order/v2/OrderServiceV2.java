package com.example.springcoreadvance.order.v2;

import com.example.springcoreadvance.trace.TraceId;
import com.example.springcoreadvance.trace.TraceStatus;
import com.example.springcoreadvance.trace.v2.TraceV2;
import org.springframework.stereotype.Service;

@Service
public record OrderServiceV2(OrderRepositoryV2 orderRepository, TraceV2 trace) {

  public void orderItem(TraceId traceId, String itemId) {
    TraceStatus status = null;
    try {
      status = trace.beginSync(traceId, "OrderServiceV2.orderItem");
      orderRepository.save(status.getTraceId(), itemId);
      trace.end(status);
    } catch (Exception e) {
      trace.exception(status, e);
      throw e;
    }
  }
}
