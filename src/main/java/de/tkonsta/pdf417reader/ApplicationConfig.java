package de.tkonsta.pdf417reader;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
public class ApplicationConfig {

  @Bean
  public MessageSource messageSource() {
    ReloadableResourceBundleMessageSource msgSrc = new ReloadableResourceBundleMessageSource();
    msgSrc.setBasename("classpath:i18n/messages");
    msgSrc.setDefaultEncoding("UTF-8");
    return msgSrc;
  }

}
