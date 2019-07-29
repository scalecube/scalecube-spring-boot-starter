# Spring Boot Scale Cube Starter

[Scale Cube](http://scalecube.io) Spring Boot Project it easy to create 
[Spring Boot](https://github.com/spring-projects/spring-boot/) application using Scale Cube.

> Scale Cube is A Novel Open-source application-platform that addresses inherent challenges 
involved in the development of distributed computing.

## Released version

You can introduce the latest dubbo-spring-boot-starter to your project by adding the 
following dependency to your pom.xml

```xml
<properties>
    <spring-boot.version>2.1.6.RELEASE</spring-boot.version>
</properties>

<dependencyManagement>
    <dependencies>
        <!-- Spring Boot -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-dependencies</artifactId>
            <version>${spring-boot.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
        
        <!-- Scale Cube -->
        <dependency>
            <groupId>io.scalecube</groupId>
            <artifactId>scalecube-bom</artifactId>
            <version>${spring-boot.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>    

<dependencies>
  <!-- Spring Boot Scale Cube Starter -->
  <dependency>
      <groupId>io.scalecube</groupId>
      <artifactId>spring-boot-starter-scalecube</artifactId>
      <version>${spring-boot.version}</version>
  </dependency>
  
  <!-- Optional. Network Transport for distributed system --> 
  <dependency>
      <groupId>io.scalecube</groupId>
      <artifactId>scalecube-services-transport-rsocket</artifactId>
  </dependency>
  <!-- Optional. Cluster & Service Discovery for distributed system -->
  <dependency>
      <groupId>io.scalecube</groupId>
      <artifactId>scalecube-services-discovery</artifactId>
  </dependency>

  <!-- Optional. Message codec for distributed system --> 
  <dependency>
      <groupId>io.scalecube</groupId>
      <artifactId>scalecube-services-transport-jackson</artifactId>
  </dependency>
</dependencies>
```

### Build from Source

If you'd like to attempt to experience latest features, you also can build from source as follow:

1. Maven install current project in your local repository.
> Maven install = `mvn install`

## Getting Started

If you don't know about Scale Cube, please take a few minutes to learn 
[www.scalecube.io](http://scalecube.io).

Usually, There are two usage scenarios for Scale Cube applications, one is Scale Cube service(s) 
provider,
another is Scale Cube service(s) consumer, thus let's get a quick start on them.

First of all, we suppose an interface as Scale Cube Service that a service provider exports 
and a service client consumes:

```java
@Service
public interface ExampleService {

  @ServiceMethod
  Mono<String> sayHello(String name);

  @ServiceMethod
  Flux<String> helloStream();

  @ServiceMethod
  Flux<MyResponse> helloBidirectional(Flux<String> nameSteam);
}
```

### Scale Cube service(s) provider

1. Service Provider implements `ExampleService`

    ```java
    @Component
    public class DefaultExampleService implements ExampleService {
      
      @Override
      public Mono<String> sayHello(String name) {
        return Mono.just("Hello, " + request);
      }
      
      @Override
      public Flux<MyResponse> helloStream() {
        return Flux.just("Hello, Eugene", "Hello, Kate");
      }
      
      @Override
      public Flux<MyResponse> helloBidirectional(Flux<String> nameSteam) {
        return nameSteam.map("Hello, "::concat);
      }
    }
    ```

2. Provides a bootstrap class

    ```java
      @SpringBootApplication
      public class ScalecubeProviderDemo {
    
          public static void main(String[] args) {
              SpringApplication.run(DubboProviderDemo.class,args);
          }
      }
    ``` 

3. Configures the `application.yml`

    ```yaml
    spring:
      scalecube:
        discovery:
          transport:
            port: 21000  
    ```

### Scale Cube Service Consumer

1. Service Consumer must depend on `ExampleService`

    ```java
    @Component
    public class ExampleServiceConsumer { 
    
      @SelectorStrategy(routerType = RoundRobinRouter)
      private final ExampleService exampleService;
      
      public ExampleServiceConsumer(ExampleService exampleService) {
        this.exampleService = exampleService;
      }
      
      public void printGreeting() {
        exampleService.helloStream().subscribe(System.out::println);
      }
    
    }
    ```
2. Service Consumer also provides a bootstrap class

    ```java
    @SpringBootApplication
    public class ScalecubeConsumerDemo {
  
        public static void main(String[] args) {
            SpringApplication.run(DubboProviderDemo.class,args);
        }
     
       @Bean
       public ApplicationRunner runner(ExampleServiceConsumer exampleServiceConsumer) {
           return args -> exampleServiceConsumer.printGreeting();
       }
    }
    ```

3. Configures `application.yml`

    ```yaml
    spring:
      scalecube:
        discovery:
          membership:
            seed-members:
              - localhost:21000
    ```
    
## Getting Help

Having trouble with Scale Cube Spring Boot? We'd like to help!

* Ask a question - You can write to [Gitter](https://gitter.im/scalecube/Lobby).
* Report bugs at 
[github.com/scalecube/spring-boot-scalecube-starter/issues](https://github.com/scalecube/spring-boot-scalecube-starter/issues).