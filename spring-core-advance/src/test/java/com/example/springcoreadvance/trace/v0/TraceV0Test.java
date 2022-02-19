package com.example.springcoreadvance.trace.v0;

import com.example.springcoreadvance.trace.TraceStatus;
import org.junit.jupiter.api.Test;

class TraceV0Test {

  @Test
  void begin() {
    TraceV0 traceV0 = new TraceV0();
    TraceStatus status = traceV0.begin("panda");
    traceV0.end(status);
  }

  @Test
  void exception() {
    TraceV0 traceV0 = new TraceV0();
    TraceStatus status = traceV0.begin("panda");
    traceV0.exception(status, new IllegalAccessException());
  }
}