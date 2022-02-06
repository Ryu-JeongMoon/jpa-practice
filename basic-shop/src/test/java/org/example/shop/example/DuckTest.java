package org.example.shop.example;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class DuckTest {

  private static Long DUCK_ID;

  @PersistenceContext
  EntityManager em;

  @BeforeEach
  void setUp() {
    Duck duck = new Duck();
    duck.setName("panda");
    em.persist(duck);
    DUCK_ID = duck.getId();
  }

  @Test
  void prePersist() {
    Duck duck = new Duck();
    duck.setName("panda");
    em.persist(duck);
    em.flush();
  }

  @Test
  void preUpdate() {
    Duck duck = em.find(Duck.class, DUCK_ID);
    duck.setName("bear");
    em.flush();
  }

  @Test
  void preRemove() {
    Duck duck = em.find(Duck.class, DUCK_ID);
    em.remove(duck);
    em.flush();
  }
}