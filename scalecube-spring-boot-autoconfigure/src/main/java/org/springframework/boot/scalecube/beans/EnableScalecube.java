package org.springframework.boot.scalecube.beans;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

/**
 * Enables Scalecube Services capability. To be used on &#064;Configuration classes as follows:
 * <br>
 * <pre class="code">
 *   &#064;Configuration
 *   &#064;EnableScalecube({RemoteService.class})
 *   public class AppConfig {
 *
 *     // various &#064;Bean definitions
 *   }
 * </pre>
 * <br>
 * This enables detection of &#064;{@link SelectionStrategy} annotation on any Spring-managed bean
 * in the container. For example, given a class {@code SomeLocalService}:
 * <pre class="code">
 * public class SomeLocalService implements LocalService {
 *
 *   private final RemoteService remoteServices;
 *
 *   public SomeLocalService(&#064;SelectionStrategy(router = "randomServiceRouter")
 *    RemoteService remoteServices) {
 *     this.remoteServices = remoteServices;
 *   }
 * }
 *  </pre>
 * <br>
 * the following configuration guarantees that the load on the RemoteService will be distributed
 * according to the algorithm defined in the "randomServiceRouter" bean:
 * <pre class="code">
 * &#064;Configuration
 * &#064;EnableScalecube(RemoteService.class)
 * public class AppConfig {
 *
 *  &#064;Bean
 *  public Router randomServiceRouter() {
 *    return new RandomServiceRouter();
 *  }
 *
 *  &#064;Bean
 *  public LocalService localService(RemoteService remoteServices) {
 *    return new SomeLocalService(remoteServices);
 *  }
 * }
 * </pre>
 * <br>
 * Annotation &#064;{@link SelectionStrategy} enables you to set custom {@link
 * io.scalecube.services.routing.Router} in any remote service. Each injected RemoteService is
 * unique (because of the prototype scope), so modifying the {@link io.scalecube.services.routing.Router}
 * does not affect other RemoteService instances. For example:
 *
 * <pre class="code">
 *   public class SomeService {
 *
 *     private final RemoteService remoteService;
 *
 *     public SomeService(&#064;SelectionStrategy(RoundRobinRouter.class)
 *      RemoteService remoteService) {
 *        this.remoteService = remoteService;
 *      }
 *   }
 * </pre>
 *
 * <pre class="code">
 *   public class SomeElseService {
 *
 *     private final RemoteService remoteService;
 *
 *     public SomeService(&#064;SelectionStrategy(RandomServiceRouter.class)
 *      RemoteService remoteService) {
 *        this.remoteService = remoteService;
 *      }
 *   }
 * </pre>
 *
 * <pre class="code">
 *   &#064;Configuration
 *   &#064;EnableScalecubeClient(remoteServices = RemoteService.class)
 *   public class AppConfig {
 *
 *     &#064;Bean
 *     public SomeService someService(RemoteService remoteService) {
 *       return new SomeService(remoteService);
 *     }
 *
 *     &#064;Bean
 *     public SomeElseService someElseService(RemoteService remoteService) {
 *       return new SomeElseService(remoteService);
 *     }
 *   }
 * </pre>
 * following, {@code SomeService} will distribute the loading between several instances of Remote
 * Service according to RoundRobin algorithm, and SomeElseService will distribute the load
 * randomly.
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(EnableScalecubeClientsImportSelector.class)
public @interface EnableScalecube {

  /**
   * Generates a client for remote services managed Spring as a bean with a scope prototype.
   *
   * @return remote service interface array
   */
  @AliasFor("value")
  Class<?>[] remoteServices() default {};

  /**
   * Alias for remoteServices.
   *
   * @return remote service interface array
   */
  @AliasFor("remoteServices")
  Class<?>[] value() default {};
}
