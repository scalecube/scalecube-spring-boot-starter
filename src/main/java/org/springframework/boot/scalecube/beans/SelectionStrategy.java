package org.springframework.boot.scalecube.beans;

import io.scalecube.services.routing.Router;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for customization router.
 */
@Target(value = {ElementType.FIELD, ElementType.PARAMETER})
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public @interface SelectionStrategy {

  /**
   * Select a router.
   *
   * @return Router class to use
   */
  Class<? extends Router> routerType() default Router.class;

  /**
   * Select a router.
   *
   * @return Router Bean Name
   */
  String router() default "";
}
