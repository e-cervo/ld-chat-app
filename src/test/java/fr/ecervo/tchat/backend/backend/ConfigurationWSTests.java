package fr.ecervo.tchat.backend.backend;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import fr.ecervo.tchat.backend.backend.exceptions.PortOutOfRangeException;
import fr.ecervo.tchat.backend.backend.ws.WebSocketConfig;

/**
 * Tests de configuration du websocket coté serveur
 */
class ConfigurationWSTests {

	

	ApplicationContextRunner context(String... properties) {
		return new ApplicationContextRunner()
				.withPropertyValues("websocket.source.protocol = " + properties[0])
				.withPropertyValues("websocket.source.allowed.url = " + properties[1])
				.withPropertyValues("websocket.source.allowed.port = " + properties[2])
				.withUserConfiguration(WebSocketConfig.class);
	}

	@Test
	@Order(1)
	void testIfPortIsUpToMax() {
		String port = "999999";
		context("http", "localhost", port).run(c -> {
			assertThat(c).getFailure().hasCause(new PortOutOfRangeException(port));
		});
	}

	@Test
	@Order(2)
	void testIfPortIsBelowZero() {
		String port = "-1";
		context("http", "localhost", port).run(c -> {
			assertThat(c).getFailure().hasCause(new PortOutOfRangeException(port));
		});
	}

	@Test
	@Order(3)
	void testIfContextIsRunning() {
		String port = "6000";
		context("http", "localhost", port).run(c -> {
			assertThat(c).hasNotFailed();
		});
	}

}
