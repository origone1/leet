package com.leetbot.leet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
public class LeetApplication {
	public static void main(String[] args) throws TelegramApiException {
		SpringApplication.run(LeetApplication.class, args);
		registerBot();
	}

	@Bean
	@Primary
	public TelegramBot telegramBot() {
		return new TelegramBot();
	}

	private static void registerBot() throws TelegramApiException {
		TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
		try {
			botsApi.registerBot(new TelegramBot());
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}
}
