# Native Image Configuration

This directory contains GraalVM native-image configuration files for building native executables.

## Build Commands

### Local Native Compilation (requires GraalVM installed)
```bash
mvn -Pnative native:compile
```

### Container-based Native Image (no local GraalVM needed)
```bash
mvn -Pnative spring-boot:build-image
```

### Run the container
```bash
docker run -p 8080:8080 fuse-starter-java:0.0.1-SNAPSHOT
```

## Configuration Files

- **reflect-config.json**: Reflection configuration for classes accessed via reflection
- **resource-config.json**: Resources to include in the native image
- **serialization-config.json**: Serialization configuration
- **dynamic-proxy-config.json**: Dynamic proxy configuration

## Generating Configuration with Native Agent

If you encounter runtime errors in the native image, you can generate additional configuration by running tests with the native agent:

```bash
# Run tests with agent to generate configuration
mvn -Pnative -Dagent=true test

# The generated config will be in target/native-agent-config/
# Review and merge relevant parts into the config files in this directory
```

## Spring Boot AOT Processing

Spring Boot 3+ includes AOT (Ahead-of-Time) processing that automatically generates most native configuration. The files in this directory are for:

1. **Manual overrides**: When Spring Boot's AOT doesn't detect something
2. **Third-party libraries**: Libraries without Spring Boot native hints (ActiveMQ, POI, etc.)
3. **Dynamic behavior**: Runtime-determined reflection or resources

## Troubleshooting

### Common Issues

1. **Missing reflection configuration**: Look for `ClassNotFoundException` or `NoSuchMethodException`
   - Add the class/method to `reflect-config.json`

2. **Missing resources**: Look for `FileNotFoundException` for classpath resources
   - Add the resource pattern to `resource-config.json`

3. **Serialization errors**: Look for serialization-related exceptions
   - Add the class to `serialization-config.json`

4. **Proxy errors**: Look for proxy creation failures
   - Add the interfaces to `dynamic-proxy-config.json`

### Enable verbose output
The native profile already includes `--verbose` flag for detailed build information.

### Report exception stack traces
The native profile includes `-H:+ReportExceptionStackTraces` to help debug native compilation issues.

