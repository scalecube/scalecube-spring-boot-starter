package org.springframework.boot.scalecube.beans;

import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.MultiValueMap;

public class EnableScalecubeClientsImportSelector implements ImportSelector {

  private static final String[] IMPORTS = {
      ScalecubeClientsBeanRegistrar.class.getName(),
      ServiceCallAwaringBeanPostProcessorRegistrar.class.getName()
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
      ScalecubeClientInvocationHandler handler = new ScalecubeClientInvocationHandler(type);
      @SuppressWarnings("unchecked")
      T proxy = (T) Proxy
          .newProxyInstance(this.getClass().getClassLoader(),
              new Class[]{type, ServiceCallAware.class}, handler);
      GenericBeanDefinition bd = new GenericBeanDefinition();
      bd.setBeanClass(type);
      bd.setInstanceSupplier(() -> proxy);
      registry.registerBeanDefinition(type.getSimpleName(), bd);
    }

  }

  public static class ServiceCallAwaringBeanPostProcessorRegistrar implements
      ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
        BeanDefinitionRegistry registry) {
      if (!registry.containsBeanDefinition(
          ServiceCallAwareBeaFactoryProcessor.BEAN_NAME)) {
        registerServiceCallAwaringBeanPostProcessor(registry);
      }
    }

    private void registerServiceCallAwaringBeanPostProcessor(
        BeanDefinitionRegistry registry) {
      GenericBeanDefinition definition = new GenericBeanDefinition();
      definition.setBeanClass(ServiceCallAwareBeaFactoryProcessor.class);
      definition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
      registry.registerBeanDefinition(
          ServiceCallAwareBeaFactoryProcessor.BEAN_NAME, definition);

    }

  }
}
