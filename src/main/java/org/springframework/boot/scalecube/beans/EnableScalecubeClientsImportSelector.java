package org.springframework.boot.scalecube.beans;

import static org.springframework.beans.factory.config.BeanDefinition.ROLE_INFRASTRUCTURE;
import static org.springframework.boot.scalecube.beans.ExternalServiceBeanFactoryPostProcessor.ESS_BFPP_BEAN_NAME;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
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
      ExternalServiceBeanFactoryPostProcessorRegistrar.class.getName(),
      ScalecubeClientsBeanRegistrar.class.getName()
  };
  private static final String EXTERNAL_SERVICE_FACTORY_BEAN_NAME = "externalServiceFactory";

  @Override
  public String[] selectImports(AnnotationMetadata metadata) {
    return IMPORTS;
  }

  public static class ScalecubeClientsBeanRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata,
        BeanDefinitionRegistry registry) {

      if (!registry.containsBeanDefinition(EXTERNAL_SERVICE_FACTORY_BEAN_NAME)) {
        GenericBeanDefinition factoryBd = new GenericBeanDefinition();
        factoryBd.setBeanClass(ProxyExternalServiceFactory.class);
        factoryBd.setSynthetic(true);
        registry.registerBeanDefinition(EXTERNAL_SERVICE_FACTORY_BEAN_NAME, factoryBd);
      }

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
      if (registry.containsBeanDefinition(type.getSimpleName())) {
        return;
      }
      GenericBeanDefinition bd = new GenericBeanDefinition();
      bd.setScope(BeanDefinition.SCOPE_PROTOTYPE);
      bd.setAttribute("external-service", true);
      ConstructorArgumentValues z = new ConstructorArgumentValues();
      z.addIndexedArgumentValue(0, type);
      bd.setConstructorArgumentValues(z);
      bd.setFactoryBeanName(EXTERNAL_SERVICE_FACTORY_BEAN_NAME);
      bd.setFactoryMethodName("createService");

      registry.registerBeanDefinition(type.getSimpleName(), bd);
    }

  }

  static class ExternalServiceBeanFactoryPostProcessorRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
        BeanDefinitionRegistry registry) {
      if (!registry.containsBeanDefinition(ESS_BFPP_BEAN_NAME)) {
        GenericBeanDefinition bd = new GenericBeanDefinition();
        bd.setSynthetic(true);
        bd.setRole(ROLE_INFRASTRUCTURE);
        bd.setBeanClass(ExternalServiceBeanFactoryPostProcessor.class);
        registry.registerBeanDefinition(ESS_BFPP_BEAN_NAME, bd);
      }
    }
  }

  static class ProxyExternalServiceFactory implements BeanFactoryAware {

    private BeanFactory beanFactory;

    @SuppressWarnings({"unchecked", "unused"})
    public <T> T createService(Class<T> type) {
      InvocationHandler handler = new ScalecubeClientInvocationHandler(
          beanFactory, type);
      return (T) Proxy
          .newProxyInstance(this.getClass().getClassLoader(),
              new Class[]{type, RouterConsumer.class}, handler);
    }


    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
      this.beanFactory = beanFactory;
    }
  }
}
