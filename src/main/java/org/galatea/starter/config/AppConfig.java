package org.galatea.starter.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import feign.Logger;
import lombok.extern.slf4j.Slf4j;
import net.sf.aspect4log.aspect.LogAspect;
import org.galatea.starter.domain.SettlementMission;
import org.galatea.starter.service.AgreementTransformer;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Slf4j
@Configuration
@EnableAspectJAutoProxy
@EnableCaching
@EnableFeignClients(basePackages = "org.galatea.starter.service")
public class AppConfig {

  /**
   * Create a LogAspect for use with the SpringAOP @Log annotation.
   */
  @Bean
  public LogAspect createLogAspect() {
    return new LogAspect();
  }

  /**
   * Returns an anonymous class implementing the IAgreementTransformer interface. Demonstrates the
   * use of a lambda function which can stand in as an anonymous class with a single method:
   * https://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html
   */
  @Bean
  public AgreementTransformer agreementTransformer() {
    return agreement -> SettlementMission.builder().instrument(agreement.getInstrument())
        .externalParty(agreement.getExternalParty()).depot("DTC").qty(agreement.getQty())
        .direction("B".equals(agreement.getBuySell()) ? "REC" : "DEL").version(0L).build();
  }

  /**
   * Set the Feign log level for interfaces annotated with @FeignClient.
   *
   * @return the Feign log level.
   */
  @Bean
  public Logger.Level logLevel() {
    return Logger.Level.BASIC;
  }

  /**
   * Manually added instead of using application properties because tests ignore it.
   *
   * <p>Example configuration:
   * spring.cache.type=caffeine
   * spring.cache.caffeine.spec=maximumSize=16384,expireAfterAccess=20m</p>
   *
   * @return caffeine CacheManager
   */
  @Bean
  public CacheManager cacheManager() {
    CaffeineCacheManager cacheManager = new CaffeineCacheManager();
    cacheManager.setCaffeine(Caffeine.newBuilder()
        .maximumSize(16384)
        .expireAfterAccess(20, java.util.concurrent.TimeUnit.MINUTES));
    return cacheManager;
  }

}
