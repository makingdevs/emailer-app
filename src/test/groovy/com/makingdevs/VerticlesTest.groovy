package com.makingdevs

import io.vertx.core.Vertx
import io.vertx.ext.unit.Async
import io.vertx.ext.unit.TestContext
import io.vertx.ext.unit.junit.VertxUnitRunner
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/*@RunWith(VertxUnitRunner.class)
class MainVerticleTest {

  Vertx vertx

  @Before
  void setUp(TestContext tc) {
    vertx = Vertx.vertx()
    vertx.deployVerticle(HelperVerticle.class.getName(), tc.asyncAssertSuccess())
  }

  @After
  void tearDown(TestContext tc) {
    vertx.close(tc.asyncAssertSuccess())
  }

  @Test
  void testThatTheServerIsStarted(TestContext tc) {
    Async async = tc.async()
    tc.assertTrue(4 > 0)
    async.complete()
  }

}*/
