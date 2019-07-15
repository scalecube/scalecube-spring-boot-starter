package org.springframework.boot.scalecube.beans;

import static org.springframework.beans.factory.config.BeanDefinition.ROLE_INFRASTRUCTURE;

import io.scalecube.services.Microservices;
import io.scalecube.services.annotations.Service;
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
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

/**
 * Register bean definition for {@link EnableScalecubeClients}.
 *
 * @author <a ref="https://github.com/eutkin">eutkin</a>
 */
public class EnableScalecubeClientsImportSelector implements ImportSelector {

  /**
   * Attribute of all remote service bean definition. Need to distinguish between local and remote
   * service when we register beans in {@link Microservices Scalecube}.
   */
  static final String REMOTE_SERVICE_ATTRIBUTE = "is-remote-service-bd";

  private static final String[] IMPORTS = {
      ExternalServiceBeanFactoryPostProcessorRegistrar.class.getName(),
      SelectionStrategyPostProcessorPostProcessorRegistrar.class.getName(),
      RemoteServiceClientsBeanRegistrar.class.getName(),
      MicroservicesFactoryRegistrar.class.getName()
  };

  private static final String EXTERNAL_SERVICE_FACTORY_BEAN_NAME = "externalServiceFactory";

  /**
   * {@inheritDoc}
   */
  @Override
  public String[] selectImports(AnnotationMetadata metadata) {
    return IMPORTS;
  }


  /**
   * Register bean definition of remote service client listed in {@link
   * EnableScalecubeClients#remoteServices()}.
   * <br>
   * For each interface, a proxy object will be created using the {@link ProxyExternalServiceFactory
   * factory}. Each proxy object also implements the RouterConsumer interface, necessary for a
   * possible router injection.
   */
  public static class RemoteServiceClientsBeanRegistrar implements ImportBeanDefinitionRegistrar {

    /**
     * Register Factory for remote services and proxy objects bean definitions.
     *
     * @param metadata source of list remote service interface.
     * @param registry bean definition registry.
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata,
        BeanDefinitionRegistry registry) {

      if (!registry.containsBeanDefinition(EXTERNAL_SERVICE_FACTORY_BEAN_NAME)) {
        GenericBeanDefinition factoryBd = new GenericBeanDefinition();
        factoryBd.setBeanClass(ProxyExternalServiceFactory.class);
        factoryBd.setSynthetic(true);
        factoryBd.setSource(this);
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
      return values.stream().flatMap(value -> Arrays.stream((Object[]) value))
          .map(o -> (Class<?>) o)
          .collect(Collectors.toList());
    }

    private <T> void register(BeanDefinitionRegistry registry, Class<T> type) {
      String serviceBeanName;
      Service annotation = AnnotationUtils.findAnnotation(type, Service.class);
      if (annotation != null && !annotation.value().isEmpty()) {
        serviceBeanName = annotation.value();
      } else {
        serviceBeanName = StringUtils.uncapitalize(type.getName());
      }
      if (registry.containsBeanDefinition(serviceBeanName)) {
        return;
      }
      GenericBeanDefinition bd = new GenericBeanDefinition();
      bd.setScope(BeanDefinition.SCOPE_PROTOTYPE);
      bd.setAttribute(REMOTE_SERVICE_ATTRIBUTE, true);
      bd.setSource(this);
      ConstructorArgumentValues z = new ConstructorArgumentValues();
      z.addIndexedArgumentValue(0, type);
      bd.setConstructorArgumentValues(z);
      bd.setFactoryBeanName(EXTERNAL_SERVICE_FACTORY_BEAN_NAME);
      bd.setFactoryMethodName("createService");

      registry.registerBeanDefinition(serviceBeanName, bd);
    }

  }

  /**
   * Register bean definition for {@link RemoteServiceBeanFactoryPostProcessor}.
   */
  static class ExternalServiceBeanFactoryPostProcessorRegistrar implements
      ImportBeanDefinitionRegistrar {

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
        BeanDefinitionRegistry registry) {
      if (!registry.containsBeanDefinition(RemoteServiceBeanFactoryPostProcessor.BEAN_NAME)) {
        GenericBeanDefinition bd = new GenericBeanDefinition();
        bd.setSynthetic(true);
        bd.setRole(ROLE_INFRASTRUCTURE);
        bd.setSource(this);
        bd.setBeanClass(RemoteServiceBeanFactoryPostProcessor.class);
        registry.registerBeanDefinition(RemoteServiceBeanFactoryPostProcessor.BEAN_NAME, bd);
      }
    }
  }

  /**
   * Register bean definition for {@link SelectionStrategyPostProcessor}.
   */
  static class SelectionStrategyPostProcessorPostProcessorRegistrar implements
      ImportBeanDefinitionRegistrar {

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
        BeanDefinitionRegistry registry) {
      if (!registry.containsBeanDefinition(SelectionStrategyPostProcessor.BEAN_NAME)) {
        GenericBeanDefinition bd = new GenericBeanDefinition();
        bd.setBeanClass(SelectionStrategyPostProcessor.class);
        bd.setRole(ROLE_INFRASTRUCTURE);
        bd.setSynthetic(true);
        bd.setSource(this);
        registry.registerBeanDefinition(SelectionStrategyPostProcessor.BEAN_NAME, bd);
      }
    }
  }

  /**
   * Register bean definition for {@link MicroservicesFactory}.
   */
  static class MicroservicesFactoryRegistrar implements ImportBeanDefinitionRegistrar {

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
        BeanDefinitionRegistry registry) {
      if (!registry.containsBeanDefinition(MicroservicesFactory.BEAN_NAME)) {
        GenericBeanDefinition bd = new GenericBeanDefinition();
        bd.setBeanClass(MicroservicesFactory.class);
        bd.setRole(ROLE_INFRASTRUCTURE);
        bd.setSynthetic(true);
        bd.setSource(this);
        registry.registerBeanDefinition(MicroservicesFactory.BEAN_NAME, bd);
      }
    }
  }

  /**
   * Factory for remote service clients. Generates implementation for input interface.
   *
   * @see RemoteServiceClientInvocationHandler
   * @see RouterConsumer
   */
  static class ProxyExternalServiceFactory implements BeanFactoryAware {

    private BeanFactory beanFactory;

    /**
     * Generates proxy for remote service interface.
     *
     * @param type remote service interface
     * @param <T> type of interface
     * @return proxy object
     */
    @SuppressWarnings({"unchecked", "unused"})
    public <T> T createService(Class<T> type) {
      InvocationHandler handler = new RemoteServiceClientInvocationHandler(
          beanFactory, type);
      return (T) Proxy
          .newProxyInstance(this.getClass().getClassLoader(),
              new Class[]{type, RouterConsumer.class}, handler);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
      this.beanFactory = beanFactory;
    }
  }
}
