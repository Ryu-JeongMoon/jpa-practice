package com.example.springcoreadvance.order.v2;

import com.example.springcoreadvance.trace.TraceId;
import com.example.springcoreadvance.trace.TraceStatus;
import com.example.springcoreadvance.trace.v2.TraceV2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryV2 {

  private final TraceV2 trace;

  public void save(TraceId traceId, String itemId) {
    TraceStatus status = null;
    try {
      status = trace.beginSync(traceId, "OrderRepositoryV2.save");

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
