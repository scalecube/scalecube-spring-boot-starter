package org.springframework.boot.scalecube.beans;

import io.scalecube.services.ServiceCall;
import io.scalecube.services.routing.Router;
import org.springframework.beans.factory.Aware;

interface ServiceCallAware extends Aware {

  void setServiceCall(ServiceCall serviceCall);

  void setRouter(Class<? extends Router> router);

}
