package com.example.datajpatest.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.datajpatest.DbConfig;
import com.example.datajpatest.entity.User;
import com.example.datajpatest.repo.UserRepo;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
// чтобы не использовать H2
@AutoConfigureTestDatabase(replace = Replace.NONE)
// чтобы сконфигурить датасорс, в основном приложении конфигурация вовсе отсутствует
@Import(DbConfig.class)
// показать где репозитории (опционально)
@EnableJpaRepositories(basePackages = "com.example.datajpatest.repo")
// показать где Entity (опционально)
@EntityScan(basePackages = "com.example.datajpatest.entity")
// подменить конфиг, чтобы случайно тест не ушел на продакшен БД
@ActiveProfiles("test-jpa")
class DataJpaTestApplicationTests {

  @Autowired
  private TestEntityManager entityManager;
  @Autowired
  private UserRepo userRepo;

  /**
   * Данные запишутся в БД, но потом транзакция откатится.
   */
  @Test
  void doSomeTest() {

    // given
    var user = new User();
    user.setName("XXX");

    // when
    user = userRepo.save(user);
    var totalCount = userRepo.count();

    // then
    assertEquals("XXX", user.getName());

    // сработает ТОЛЬКО первый раз. При откате транзакции identity не откатывается и будет увеличиваться
    // assertEquals(1, user.getId());
    assertThat(user.getCreatedAt()).isAfter(Instant.now().minusSeconds(20));
    assertEquals(1L, totalCount);
  }

}
