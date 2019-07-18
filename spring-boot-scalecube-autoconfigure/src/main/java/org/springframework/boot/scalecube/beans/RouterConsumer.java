package org.springframework.boot.scalecube.beans;

import io.scalecube.services.routing.Router;

/**
 * Consume {@link Router}.
 */
public interface RouterConsumer {

  /**
   * Setting custom router.
   *
   * @param router router
   */
  void setRouter(Router router);

}
