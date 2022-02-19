package com.example.springcoreadvance.trace.v2;

import com.example.springcoreadvance.trace.TraceId;
import com.example.springcoreadvance.trace.TraceStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TraceV2 {

  private static final String START_PREFIX = "-->";
  private static final String COMPLETE_PREFIX = "<--";
  private static final String EX_PREFIX = "<X-";

  private static String addSpace(String prefix, int level) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < level; i++) {
      sb.append((i == level - 1) ? "|" + prefix : "|   ");
    }
    return sb.toString();
  }

  public TraceStatus begin(String message) {
    TraceId traceId = new TraceId();
    Long startTimeMs = System.currentTimeMillis();
    log.info(
      "[" + traceId.getId() + "] " +
        addSpace(START_PREFIX, traceId.getLevel()) +
        message
    );
    return new TraceStatus(traceId, message, startTimeMs);
  }


  public TraceStatus beginSync(TraceId beforeTraceId, String message) {
    TraceId nextId = beforeTraceId.createNextId();
    Long startTimeMs = System.currentTimeMillis();
    log.info(
      "[" + nextId.getId() + "] " +
        addSpace(START_PREFIX, nextId.getLevel()) +
        message
    );
    return new TraceStatus(nextId, message, startTimeMs);
  }

  public void end(TraceStatus status) {
    complete(status, null);
  }

  public void exception(TraceStatus status, Exception e) {
    complete(status, e);
  }

  private void complete(TraceStatus status, Exception e) {
    Long stopTimeMs = System.currentTimeMillis();
    long resultTimeInMillis = stopTimeMs - status.getStartTimeInMillis();
    TraceId traceId = status.getTraceId();
    if (e == null) {
      log.info(
        "[{}] {}{} time={}ms",
        traceId.getId(),
        addSpace(COMPLETE_PREFIX, traceId.getLevel()),
        status.getMessage(),
        resultTimeInMillis
      );

    } else {
      log.info(
        "[{}] {}{} time={}ms ex={}",
        traceId.getId(),
        addSpace(EX_PREFIX, traceId.getLevel()),
        status.getMessage(),
        resultTimeInMillis,
        e.toString()
      );
    }
  }
}
