# Spring Boot to Quarkus Migration Guide using windsurf and Claude Sonnet 4 model

This document outlines the key changes required when migrating a Spring Boot application to Quarkus, based on practical migration experience.

## Overview

This guide covers the migration of a Spring Boot web application with Thymeleaf templates to a Quarkus application using Qute templates and JAX-RS endpoints.

## 1. Project Configuration Changes

### Maven Dependencies

**Before (Spring Boot):**
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

**After (Quarkus):**
```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-resteasy-reactive</artifactId>
</dependency>
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-qute</artifactId>
</dependency>
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-resteasy-reactive-qute</artifactId>
</dependency>
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-arc</artifactId>
</dependency>
<!-- Test dependencies -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-junit5</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>io.rest-assured</groupId>
    <artifactId>rest-assured</artifactId>
    <scope>test</scope>
</dependency>
```

### Key Dependency Notes

- **`quarkus-resteasy-reactive-qute`** is **essential** for automatic template rendering in JAX-RS endpoints
- Without this dependency, `TemplateInstance` objects won't be rendered to HTML
- RestAssured is the preferred testing framework for HTTP endpoints in Quarkus

## 2. Application Main Class

### Before (Spring Boot):
```java
@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
```

### After (Quarkus):
```java
@QuarkusMain
public class DemoApplication implements QuarkusApplication {
    public static void main(String[] args) {
        Quarkus.run(DemoApplication.class, args);
    }

    @Override
    public int run(String... args) throws Exception {
        Quarkus.waitForExit();
        return 0;
    }
}
```

**Key Changes:**
- Replace `@SpringBootApplication` with `@QuarkusMain`
- Implement `QuarkusApplication` interface
- Use `Quarkus.run()` instead of `SpringApplication.run()`

## 3. Controller/Endpoint Migration

### Before (Spring Boot):
```java
@Controller
public class HomeController {
    
    @Value("${spring.application.name}")
    private String appName;
    
    @GetMapping("/greeting")
    public String greeting(Model model) {
        model.addAttribute("message", "Hello from " + appName + "!");
        return "index";
    }
}
```

### After (Quarkus):
```java
@Path("/")
public class HomeController {
    
    @ConfigProperty(name = "quarkus.application.name")
    String appName;
    
    @Inject
    Template index;

    @GET
    @Path("/greeting")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance greeting() {
        return index.data("message", "Hello from " + appName + "!");
    }
}
```

**Key Changes:**
- Replace `@Controller` with `@Path("/")`
- Replace `@GetMapping` with `@GET` + `@Path`
- Replace `@Value` with `@ConfigProperty`
- Inject `Template` instead of using `Model`
- Return `TemplateInstance` instead of template name string
- Add `@Produces(MediaType.TEXT_HTML)` for proper content type

## 4. Template Migration

### Before (Thymeleaf):
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Greeting Page</title>
</head>
<body>
    <div class="container">
        <h1 class="greeting" th:text="${message}"></h1>
    </div>
</body>
</html>
```

### After (Qute):
```html
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Greeting Page</title>
</head>
<body>
    <div class="container">
        <h1 class="greeting">{message}</h1>
    </div>
</body>
</html>
```

**Key Changes:**
- Remove Thymeleaf namespace declaration
- Replace `th:text="${variable}"` with `{variable}`
- Qute uses simpler syntax with curly braces

## 5. Test Migration

### Before (Spring Boot):
```java
@SpringBootTest
class DemoApplicationTests {

    @Test
    void contextLoads() {
    }
}
```

### After (Quarkus):
```java
@QuarkusTest
class DemoApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void testGreetingEndpoint() {
        given()
            .when().get("/greeting")
            .then()
            .statusCode(200)
            .contentType("text/html")
            .body(containsString("Hello from"));
    }
}
```

**Key Changes:**
- Replace `@SpringBootTest` with `@QuarkusTest`
- Import `io.quarkus.test.junit.QuarkusTest`
- Use RestAssured for HTTP endpoint testing with `given().when().then()` pattern
- Import static methods: `import static io.restassured.RestAssured.given;`

## 6. Configuration Properties

### Before (Spring Boot):
```properties
# application.properties
spring.application.name=My Spring Boot App
```

### After (Quarkus):
```properties
# application.properties
quarkus.application.name=Bootstrap Quarkus
```

**Key Changes:**
- Replace `spring.*` properties with `quarkus.*` equivalents

## 7. Common Migration Patterns

### Dependency Injection
- **Spring**: `@Autowired`, `@Value`
- **Quarkus**: `@Inject`, `@ConfigProperty`

### REST Endpoints
- **Spring**: `@RestController`, `@GetMapping`, `@PostMapping`
- **Quarkus**: `@Path`, `@GET`, `@POST`, `@Produces`, `@Consumes`

### Template Engines
- **Spring**: Thymeleaf with `th:*` attributes
- **Quarkus**: Qute with `{variable}` syntax

### Testing
- **Spring**: `@SpringBootTest`, MockMvc
- **Quarkus**: `@QuarkusTest`, RestAssured

## 8. Critical Dependencies for Template Rendering

⚠️ **Important**: When using Qute templates with JAX-RS endpoints, you **must** include:

```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-resteasy-reactive-qute</artifactId>
</dependency>
```

Without this dependency, `TemplateInstance` objects will not be automatically rendered to HTML, causing tests and endpoints to return object references instead of rendered content.

## 8.1 Template Variable Mapping

When migrating from Thymeleaf to Qute, ensure the controller data mapping matches the template variables:

**Controller**: `return index.data("message", "Hello from " + appName + "!");`
**Template**: `{message}` (not `{appName}`)

The data key in the controller must match the variable name in the template.

## 9. Migration Checklist

- [ ] Update Maven dependencies from Spring Boot to Quarkus
- [ ] Add `quarkus-resteasy-reactive-qute` for template rendering
- [ ] Migrate main application class to use `@QuarkusMain`
- [ ] Convert controllers to JAX-RS resources with `@Path`
- [ ] Update dependency injection annotations
- [ ] Convert Thymeleaf templates to Qute syntax
- [ ] Migrate tests from `@SpringBootTest` to `@QuarkusTest`
- [ ] Update configuration properties from `spring.*` to `quarkus.*`
- [ ] Test endpoint functionality with RestAssured

## 10. Migration Process & Order

### Optional: Restore Original Spring Boot Application

If you want to practice the migration from scratch or reset to the original Spring Boot state:

```bash
git reset --hard HEAD
```

⚠️ **Warning**: This command will permanently discard all uncommitted changes. Only use if you want to start the migration from the beginning.

**Critical**: Follow this exact order to avoid compilation issues:

1. **Dependencies First**: Update `pom.xml` completely before touching Java files
2. **Main Class**: Convert application entry point
3. **Controllers**: Migrate Spring MVC to JAX-RS
4. **Templates**: Convert Thymeleaf to Qute syntax  
5. **Tests**: Update test annotations and framework
6. **Configuration**: Update properties files
7. **Build & Validate**: Run `mvn clean install` to verify

**Note**: IDE will show import errors during migration - this is expected until Maven resolves new dependencies.

## 11. Troubleshooting

### Template Not Rendering
**Problem**: Endpoint returns `TemplateInstance` object instead of HTML
**Solution**: Add `quarkus-resteasy-reactive-qute` dependency

### Test Compilation Errors  
**Problem**: `SpringBootTest cannot be resolved to a type`
**Solution**: Replace with `@QuarkusTest` and update imports

### Template Syntax Errors
**Problem**: Thymeleaf expressions not working  
**Solution**: Convert `th:text="${var}"` to `{var}` for Qute

### IDE Import Errors During Migration
**Problem**: `io.quarkus cannot be resolved` errors during migration
**Solution**: Expected behavior - complete all file changes, then run `mvn clean install`

### Template Variable Mismatch
**Problem**: Template shows variable names instead of values
**Solution**: Ensure controller `.data("key", value)` matches template `{key}`

This guide provides a comprehensive overview of the key changes required for Spring Boot to Quarkus migration based on real-world migration experience.
