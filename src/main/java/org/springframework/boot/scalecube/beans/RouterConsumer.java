package org.springframework.boot.scalecube.beans;

import io.scalecube.services.routing.Router;

interface RouterConsumer {

  void setRouter(Class<? extends Router> router);

}
