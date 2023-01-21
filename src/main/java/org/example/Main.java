package org.example;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.IOException;
import java.util.Scanner;

public class Main  {

    public static void main(String[] args) throws TelegramApiException {
        db_Connect db_connect = new db_Connect();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        TelegramBot bot = new TelegramBot();
        telegramBotsApi.registerBot(bot);
        Administration admin = new Administration();
        Scanner in = new Scanner(System.in);
        while (true)
        {
            admin.command_handler(in.nextLine(), bot);
        }
    }


}