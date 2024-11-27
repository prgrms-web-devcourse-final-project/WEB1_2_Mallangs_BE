package com.mallangs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing // JPA Auditing 기능 활성화
public class MallangsApplication {

  public static void main(String[] args) {
    SpringApplication.run(MallangsApplication.class, args);
    System.out.println();
    System.out.println(org.hibernate.Version.getVersionString());
//    implementation group: 'org.hibernate', name: 'hibernate-spatial', version: '5.6.9.Final'
  }

}
