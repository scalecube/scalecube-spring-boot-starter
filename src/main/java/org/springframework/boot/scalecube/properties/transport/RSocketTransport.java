package org.springframework.boot.scalecube.properties.transport;

import java.util.Optional;

public class RSocketTransport {

  private Integer workerCount;

  public Optional<Integer> getWorkerCount() {
    return Optional.ofNullable(workerCount);
  }

  public void setWorkerCount(Integer workerCount) {
    this.workerCount = workerCount;
  }
}
