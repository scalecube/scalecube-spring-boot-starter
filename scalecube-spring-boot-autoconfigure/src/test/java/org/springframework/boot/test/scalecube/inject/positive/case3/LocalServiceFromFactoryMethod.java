package org.springframework.boot.test.scalecube.inject.positive.case3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.scalecube.example.SecondLocalService;
import org.springframework.boot.test.scalecube.example.ThirdLocalService;

class LocalServiceFromFactoryMethod implements SecondLocalService {

  private final ThirdLocalService injectedThroughConstructor;

  @Autowired
  private ThirdLocalService injectedThroughField;

  private ThirdLocalService injectedThroughSetter;

  LocalServiceFromFactoryMethod(ThirdLocalService injectedThroughConstructor) {
    this.injectedThroughConstructor = injectedThroughConstructor;
  }

  @Autowired
  public void setInjectedThroughSetter(ThirdLocalService injectedThroughSetter) {
    this.injectedThroughSetter = injectedThroughSetter;
  }

  public ThirdLocalService getInjectedThroughConstructor() {
    return injectedThroughConstructor;
  }

  public ThirdLocalService getInjectedThroughField() {
    return injectedThroughField;
  }

  public ThirdLocalService getInjectedThroughSetter() {
    return injectedThroughSetter;
  }
}
