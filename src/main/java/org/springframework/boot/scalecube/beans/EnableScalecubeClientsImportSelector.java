package org.springframework.boot.scalecube.beans;

import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.MultiValueMap;

public class EnableScalecubeClientsImportSelector implements ImportSelector {

  private static final String[] IMPORTS = {
      ScalecubeClientsBeanRegistrar.class.getName(),
      ServiceCallAwareBeanPostProcessorRegistrar.class.getName()
  };

  @Override
  public String[] selectImports(AnnotationMetadata metadata) {
    return IMPORTS;
  }

  public static class ScalecubeClientsBeanRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata,
        BeanDefinitionRegistry registry) {

      getTypes(metadata).forEach(type -> register(registry, type));
    }

    private List<Class<?>> getTypes(AnnotationMetadata metadata) {
      MultiValueMap<String, Object> attributes = metadata
          .getAllAnnotationAttributes(
              EnableScalecubeClients.class.getName(), false);
      return collectClasses((attributes != null) ? attributes.get("value")
          : Collections.emptyList());
    }

    private List<Class<?>> collectClasses(List<?> values) {
      return values.stream().flatMap((value) -> Arrays.stream((Object[]) value))
          .map((o) -> (Class<?>) o).filter((type) -> void.class != type)
          .collect(Collectors.toList());
    }

    private <T> void register(BeanDefinitionRegistry registry, Class<T> type) {

      GenericBeanDefinition handlerBd = new GenericBeanDefinition();
      handlerBd.setBeanClass(ScalecubeClientInvocationHandler.class);
      handlerBd.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
      handlerBd.setScope(BeanDefinition.SCOPE_PROTOTYPE);
      ConstructorArgumentValues x = new ConstructorArgumentValues();
      x.addIndexedArgumentValue(0, type);
      handlerBd.setConstructorArgumentValues(x);
      registry.registerBeanDefinition("proxyStateHolder" + type.getSimpleName(), handlerBd);

      @SuppressWarnings("unchecked")
      T proxyService = (T) Proxy
          .newProxyInstance(this.getClass().getClassLoader(),
              new Class[]{type, ServiceCallAware.class}, (proxy, method, args) -> null);
      GenericBeanDefinition bd = new GenericBeanDefinition();
      bd.setScope(BeanDefinition.SCOPE_PROTOTYPE);
      bd.setBeanClass(proxyService.getClass());
      bd.setAttribute("external-service", true);
      bd.setDependsOn("microservices");
      registry.registerBeanDefinition(type.getSimpleName(), bd);
    }

  }

  public static class ServiceCallAwareBeanPostProcessorRegistrar implements
      ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
        BeanDefinitionRegistry registry) {
      if (!registry.containsBeanDefinition(
          ServiceCallAwarePostProcessor.BEAN_NAME)) {
        registerServiceCallAwareBeanPostProcessor(registry);
      }
    }

    private void registerServiceCallAwareBeanPostProcessor(
        BeanDefinitionRegistry registry) {
      GenericBeanDefinition definition = new GenericBeanDefinition();
      definition.setBeanClass(ServiceCallAwarePostProcessor.class);
      definition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
      definition.setDependsOn("microservices");
      registry.registerBeanDefinition(
          ServiceCallAwarePostProcessor.BEAN_NAME, definition);

    }

  }
}
