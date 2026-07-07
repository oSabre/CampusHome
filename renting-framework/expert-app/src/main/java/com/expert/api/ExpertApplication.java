package com.expert.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * scanBasePackages (an attribute of @SpringBootApplication itself) is used
 * here instead of a separate @ComponentScan annotation. Stacking an
 * explicit @ComponentScan alongside @SpringBootApplication on the same
 * class silently drops the default exclude filters @SpringBootApplication
 * normally sets up (the ones that keep autoconfiguration classes from
 * being picked up as regular beans) — scanBasePackages extends the scan
 * without that side effect.
 *
 * All three annotations need to reach into com.rentingframework.core, or
 * this fails at startup with "no qualifying bean" the moment any
 * controller needs a core service:
 *   - scanBasePackages: core's @Service/@Configuration/@RestControllerAdvice
 *     beans (UserService, RequestService, GroupService, TaskService,
 *     AIConfig, GlobalExceptionHandler).
 *   - @EntityScan: core's @Entity classes (User, Listing, Request, Group,
 *     Message, Task) — without this, Hibernate never creates their
 *     tables, and the JOINED inheritance with this app's subclasses
 *     breaks immediately.
 *   - @EnableJpaRepositories: core's repository interfaces (UserRepository,
 *     ListingRepository, RequestRepository, GroupRepository,
 *     MessageRepository, TaskRepository).
 */
@SpringBootApplication(scanBasePackages = {"com.expert.api", "com.rentingframework.core"})
@EntityScan(basePackages = {"com.expert.api.model", "com.rentingframework.core.model"})
@EnableJpaRepositories(basePackages = {"com.expert.api.repository", "com.rentingframework.core.repository"})
public class ExpertApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExpertApplication.class, args);
    }
}