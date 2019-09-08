package edu.training.jsharma;

import java.util.Collections;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.aws.messaging.config.QueueMessageHandlerFactory;
import org.springframework.cloud.aws.messaging.config.annotation.EnableSqs;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.PayloadArgumentResolver;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;

@org.springframework.boot.autoconfigure.SpringBootApplication
@EnableSqs
public class SpringBootApplication {
  public static void main(String[] args) {
    System.setProperty("aws.accessKeyId", "<accessKeyId>");
    System.setProperty("aws.secretKey", "<secretKey>");
    System.setProperty("aws.region", "us-east-1");
    SpringApplication.run(SpringBootApplication.class, args);
  }

  @Bean
  public QueueMessageHandlerFactory queueMessageHandlerFactory() {
    QueueMessageHandlerFactory factory = new QueueMessageHandlerFactory();
    MappingJackson2MessageConverter messageConverter = new MappingJackson2MessageConverter();

    // set strict content type match to false
    messageConverter.setStrictContentTypeMatch(false);
    factory.setArgumentResolvers(
        Collections.<HandlerMethodArgumentResolver>singletonList(
            new PayloadArgumentResolver(messageConverter)));
    return factory;
  }
}
