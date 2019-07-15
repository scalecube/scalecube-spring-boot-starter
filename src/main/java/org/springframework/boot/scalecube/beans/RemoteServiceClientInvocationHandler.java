package org.springframework.boot.scalecube.beans;

import io.scalecube.services.Microservices;
import io.scalecube.services.Reflect;
import io.scalecube.services.ServiceCall;
import io.scalecube.services.api.ServiceMessage;
import io.scalecube.services.methods.MethodInfo;
import io.scalecube.services.routing.Router;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.BeanFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Invocation handler for remote service client proxy.
 *
 * <p>Stores the state of the proxy object.
 * Initializes {@link ServiceCall serviceCall} at the first call of the method to receive data from
 * a remote service.
 *
 * @see EnableScalecubeClientsImportSelector
 */
public class RemoteServiceClientInvocationHandler implements InvocationHandler {

  private static final ServiceMessage UNEXPECTED_EMPTY_RESPONSE =
      ServiceMessage.error(503, 503, "Unexpected empty response");


  private final BeanFactory beanFactory;
  private final Map<Method, MethodInfo> genericReturnTypes;
  private final Class<?> serviceInterface;
  private AtomicReference<ServiceCall> serviceCallRef = new AtomicReference<>();
  private Router router;

  RemoteServiceClientInvocationHandler(BeanFactory beanFactory,
      Class<?> serviceInterface) {
    this.beanFactory = beanFactory;
    this.genericReturnTypes = Reflect.methodsInfo(serviceInterface);
    this.serviceInterface = serviceInterface;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @SuppressWarnings("unchecked")
  public Object invoke(Object proxy, Method method, Object[] params) {

    Optional<Object> check =
        toStringOrEqualsOrHashCode(method.getName(), serviceInterface, params);
    if (check.isPresent()) {
      return check.get(); // toString, hashCode was invoked.
    }

    if ("setRouter".equals(method.getName()) && Objects.nonNull(params)
        && params.length == 1) {
      this.router = (Router) params[0];
      return null;
    }

    ServiceCall serviceCall = serviceCallRef.updateAndGet(this::createOrGet);

    final MethodInfo methodInfo = genericReturnTypes.get(method);
    final Type returnType = methodInfo.parameterizedReturnType();
    final boolean isServiceMessage = methodInfo.isRequestTypeServiceMessage();

    switch (methodInfo.communicationMode()) {
      case FIRE_AND_FORGET:
        return serviceCall.oneWay(toServiceMessage(methodInfo, params));

      case REQUEST_RESPONSE:
        return serviceCall
            .requestOne(toServiceMessage(methodInfo, params), returnType)
            .transform(asMono(isServiceMessage));

      case REQUEST_STREAM:
        return serviceCall
            .requestMany(toServiceMessage(methodInfo, params), returnType)
            .transform(asFlux(isServiceMessage));

      case REQUEST_CHANNEL:
        // this is REQUEST_CHANNEL so it means params[0] must be a publisher - its safe to
        // cast.
        return serviceCall
            .requestBidirectional(
                Flux.from((Publisher) params[0])
                    .map(data -> toServiceMessage(methodInfo, data)),
                returnType)
            .transform(asFlux(isServiceMessage));

      default:
        throw new IllegalArgumentException(
            "Communication mode is not supported: " + method);
    }
  }

  private ServiceCall createOrGet(ServiceCall call) {
    if (call == null) {
      Microservices microservices = beanFactory.getBean(Microservices.class);
      ServiceCall serviceCall = microservices.call();
      if (router != null) {
        return serviceCall.router(router);
      } else {
        return serviceCall;
      }
    }
    return call;
  }

  /**
   * check and handle toString or equals or hashcode method where invoked.
   *
   * @param method that was invoked.
   * @param serviceInterface for a given service interface.
   * @param args parameters that where invoked.
   * @return Optional object as result of to string equals or hashCode result or absent if none of
   * these where invoked.
   */
  private Optional<Object> toStringOrEqualsOrHashCode(
      String method, Class<?> serviceInterface, Object... args) {
    switch (method) {
      case "toString":
        return Optional.of(serviceInterface.toString());
      case "equals":
        return Optional.of(serviceInterface.equals(args[0]));
      case "hashCode":
        return Optional.of(serviceInterface.hashCode());

      default:
        return Optional.empty();
    }
  }

  private ServiceMessage toServiceMessage(MethodInfo methodInfo, Object... params) {
    if (methodInfo.parameterCount() != 0 && params[0] instanceof ServiceMessage) {
      return ServiceMessage.from((ServiceMessage) params[0])
          .qualifier(methodInfo.serviceName(), methodInfo.methodName())
          .build();
    }
    return ServiceMessage.builder()
        .qualifier(methodInfo.serviceName(), methodInfo.methodName())
        .data(methodInfo.parameterCount() != 0 ? params[0] : null)
        .build();
  }

  private Function<Flux<ServiceMessage>, Flux<Object>> asFlux(boolean isRequestTypeServiceMessage) {
    return flux -> isRequestTypeServiceMessage ? flux.cast(Object.class) : flux.map(msgToResp());
  }

  private Function<Mono<ServiceMessage>, Mono<Object>> asMono(boolean isRequestTypeServiceMessage) {
    return mono -> isRequestTypeServiceMessage ? mono.cast(Object.class) : mono.map(msgToResp());
  }

  private Function<ServiceMessage, Object> msgToResp() {
    return sm -> sm.hasData() ? sm.data() : UNEXPECTED_EMPTY_RESPONSE;
  }

}
