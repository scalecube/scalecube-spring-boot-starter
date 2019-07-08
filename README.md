# Spring Boot Scalecube Starter

## Description

This project helps to use the [Scalecube Service](scalecube.io) in the usual way in the Spring Eco system.

## Glosary

**Scalecube External Service** or **ESS** — scalecube service deployed on another node.

**Scalecube Service** or **SS** — scalecube service deployed on current node.

**Custom Bean**  — сustom spring bean what is not a scalecube service.

**Router** or **SelectionStrategy** — load balancer between nodes.

## Roadmap 

* Integration Scalecube services with Spring Ioc:
  * **ESS** injected in **Customer bean**.
  * **ESS** injected in **SS**.
  * **SS** injected in **Customer bean**.
  * **ESS** injected with custom router.
  * Supports injection via constructor, setter and field.
* User friendly configuration for Scalecube Transport, such RSocket, Aeron, Netty Tcp, etc.
* User friendly configuration for Scalecube Service Discovery.
