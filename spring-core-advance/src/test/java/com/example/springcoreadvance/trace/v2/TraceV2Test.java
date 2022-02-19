package com.example.springcoreadvance.trace.v2;

import static org.junit.jupiter.api.Assertions.*;

import com.example.springcoreadvance.trace.TraceStatus;
import com.example.springcoreadvance.trace.v1.TraceV1;
import org.junit.jupiter.api.Test;

class TraceV2Test {
  @Test
  void begin() {
    TraceV2 trace = new TraceV2();
    TraceStatus status1 = trace.begin("panda");
    TraceStatus status2 = trace.beginSync(status1.getTraceId(), "pandabear");
    trace.end(status2);
    trace.end(status1);
  }

  @Test
  void exception() {
    TraceV2 trace = new TraceV2();
    TraceStatus status1 = trace.begin("panda");
    TraceStatus status2 = trace.beginSync(status1.getTraceId(), "pandabear");
    trace.exception(status2, new IllegalStateException());
    trace.exception(status1, new IllegalStateException());
  }
}