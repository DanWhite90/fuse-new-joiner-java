package org.galatea.starter.config.graalvm;

import jakarta.jms.QueueSession;
import jakarta.jms.Session;
import jakarta.jms.TopicSession;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.jms.connection.SessionProxy;

public class JmsRuntimeHints implements RuntimeHintsRegistrar {

  @Override
  public void registerHints(final RuntimeHints hints, final ClassLoader classLoader) {
    hints.proxies().registerJdkProxy(
        SessionProxy.class,
        QueueSession.class,
        TopicSession.class
    );

    hints.reflection()
        .registerType(SessionProxy.class)
        .registerType(QueueSession.class)
        .registerType(TopicSession.class)
        .registerType(Session.class);
  }

}
