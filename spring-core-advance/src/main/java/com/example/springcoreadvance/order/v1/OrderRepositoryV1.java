package com.example.springcoreadvance.order.v1;

import com.example.springcoreadvance.trace.TraceStatus;
import com.example.springcoreadvance.trace.v0.TraceV0;
import com.example.springcoreadvance.trace.v1.TraceV1;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryV1 {

  private final TraceV1 trace;

  public void save(String itemId) {
    TraceStatus status = null;
    try {
      status = trace.begin("OrderRepositoryV1.save");

      if (itemId.equals("ex"))
        throw new IllegalStateException("예외 발생");

      sleep(1000);
      trace.end(status);
    } catch (Exception e) {
      trace.exception(status, e);
      throw e;
    }
  }

  private void sleep(int millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
