package com.example.springcoreadvance.trace.v1;

import com.example.springcoreadvance.trace.TraceStatus;
import org.junit.jupiter.api.Test;

class TraceV1Test {

  @Test
  void begin() {
    TraceV1 trace = new TraceV1();
    TraceStatus status1 = trace.begin("panda");
    TraceStatus status2 = trace.beginSync(status1.getTraceId(), "pandabear");
    trace.end(status2);
    trace.end(status1);
  }

  @Test
  void exception() {
    TraceV1 trace = new TraceV1();
    TraceStatus status1 = trace.begin("panda");
    TraceStatus status2 = trace.beginSync(status1.getTraceId(), "pandabear");
    trace.exception(status2, new IllegalStateException());
    trace.exception(status1, new IllegalStateException());
  }
}