package org.example;

import java.util.Scanner;

public class Administration {
    public void command_handler(String command, TelegramBot executor) {
        db_Connect db_connect = new db_Connect();
        db_connect.db_connect();
        Scanner in = new Scanner(System.in);
        switch (command)
        {
            case "drop":
                db_connect.db_drop();
                System.out.println("DATATABLE DROPED");
                break;

            case "msg":
                db_connect.db_selectAll();
                System.out.print("WRITE MESSEGE FOR ALL USERS: ");
                String msg = in.nextLine();
                for(Long i : db_connect.db_getAllid())
                {
                    executor.sendMsg(i,msg);
                }
                break;
            case "msgu":
                db_connect.db_selectAll();
                System.out.print("WRITE USER ID: ");
                Long id = in.nextLong();
                in.nextLine(); //костыль
                System.out.print("WRITE MESSEGE FOR THIS USER: ");
                String msgu = in.nextLine();
                executor.sendMsg(id,msgu);
                System.out.print("MESSEGE SENDED");

        }
        db_connect.db_close();
    }
}
