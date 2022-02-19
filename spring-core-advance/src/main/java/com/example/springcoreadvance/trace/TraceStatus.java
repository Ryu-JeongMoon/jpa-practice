package com.example.springcoreadvance.trace;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TraceStatus {

  private TraceId traceId;
  private String message;
  private Long startTimeInMillis;
}
