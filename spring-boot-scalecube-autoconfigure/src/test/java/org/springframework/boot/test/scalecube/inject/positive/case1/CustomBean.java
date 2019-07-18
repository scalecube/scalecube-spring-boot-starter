package org.springframework.boot.test.scalecube.inject.positive.case1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.scalecube.example.RemoteServiceClient;
import org.springframework.stereotype.Component;

@Component
class CustomBean {

  private final RemoteServiceClient injectedThroughConstructor;

  @Autowired
  private RemoteServiceClient injectedThroughField;

  private RemoteServiceClient injectedThroughSetter;

  CustomBean(RemoteServiceClient injectedThroughConstructor) {
    this.injectedThroughConstructor = injectedThroughConstructor;
  }

  @Autowired
  public void setInjectedThroughSetter(RemoteServiceClient injectedThroughSetter) {
    this.injectedThroughSetter = injectedThroughSetter;
  }

  public RemoteServiceClient getInjectedThroughConstructor() {
    return injectedThroughConstructor;
  }

  public RemoteServiceClient getInjectedThroughField() {
    return injectedThroughField;
  }

  public RemoteServiceClient getInjectedThroughSetter() {
    return injectedThroughSetter;
  }
}
