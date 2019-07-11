package org.springframework.boot.test.scalecube.inject.positive.case3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.scalecube.example.FirstLocalService;
import org.springframework.boot.test.scalecube.example.ThirdLocalService;
import org.springframework.stereotype.Component;

@Component
class FirstLocalServiceImpl implements FirstLocalService {

  private final ThirdLocalService injectedThroughConstructor;

  @Autowired
  private ThirdLocalService injectedThroughField;

  private ThirdLocalService injectedThroughSetter;

  FirstLocalServiceImpl(ThirdLocalService injectedThroughConstructor) {
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
