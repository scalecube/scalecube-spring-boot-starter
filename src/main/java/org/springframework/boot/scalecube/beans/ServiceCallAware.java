package org.springframework.boot.scalecube.beans;

import io.scalecube.services.ServiceCall;
import org.springframework.beans.factory.Aware;

public interface ServiceCallAware extends Aware {

  void setServiceCall(ServiceCall serviceCall);

}
