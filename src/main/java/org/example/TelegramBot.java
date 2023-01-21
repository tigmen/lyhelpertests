package org.example;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.ws.rs.HttpMethod;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TelegramBot extends TelegramLongPollingBot{
    db_Connect db_connect = new db_Connect();


    public TelegramBot() {

    }

    @Override
    public String getBotUsername() {
        return Const.telegram.USERNAME;
    }

    @Override
    public String getBotToken() {
        return Const.telegram.TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        Commands commands = new Commands();
        KeyboardMenu keyboardMenu = new KeyboardMenu();

        if (update.hasMessage()) {

            Message msg = update.getMessage();
            if(Const.telegram.states._id.indexOf(msg.getFrom().getId()) == -1)
            {
                Const.telegram.states._id.add(msg.getFrom().getId());
                Const.telegram.states._state.add(Const.telegram.states.STD);
            }
            switch (Const.telegram.states._state.get(state_find(msg.getFrom().getId()))) {
                case Const.telegram.states.STD:
                    db_connect.db_connect();
                    db_connect.db_insert(msg.getFrom().getId(), msg.getFrom().getUserName());
                    db_connect.db_close();
                    if (msg.isCommand()) {
                        commands.handler(msg.getText(), msg);
                    } else if (update.hasCallbackQuery()) {
                        var callback = update.getCallbackQuery();
                        keyboardMenu.handler(callback.getData(), callback.getFrom().getId(),callback.getMessage());
                    }
                    break;

                case Const.telegram.states.FILELOAD_FILE:
                    if(msg.hasDocument())
                    {
                        db_connect.db_connect();
                        db_connect.filedb_insert(msg.getFrom().getId(),msg.getFrom().getUserName(),msg.getDocument().getFileId(),
                                msg.getDocument().getFileName());
                        Const.telegram.states._state.set(state_find(msg.getFrom().getId()),Const.telegram.states.FILELOAD_NAME);
                        sendMsg(msg.getFrom().getId(),"Введите название конспекта: ");
                    }
                    else
                    {
                        sendMsg(msg.getFrom().getId(), Const.telegram.msg_strings.FILELOAD_ERROR_NOFILE);
                    }
                    db_connect.db_close();
                    break;
                case Const.telegram.states.FILELOAD_NAME:
                    db_connect.db_connect();
                    db_connect.filedb_update(db_connect.db_lastid("SELECT ID FROM " + Const.database.userfile.NAME),msg.getText(),Const.database.userfile.DOCNAME);
                    Const.telegram.states._state.set(state_find(msg.getFrom().getId()),Const.telegram.states.FILELOAD_CLASS);
                    db_connect.db_close();
                    sendMsg(msg.getFrom().getId(),"Материал какого это класса?",keyboardMenu.kb_setbtnclassload());
                    break;
                case Const.telegram.states.FILELOAD_CLASS:
                    sendMsg(msg.getFrom().getId(),"Выберите класс");
                    break;
            }
        }
        else if(update.hasCallbackQuery())
        {
            var cq = update.getCallbackQuery();
            if(Const.telegram.states._id.indexOf(cq.getFrom().getId()) == -1)
            {
                Const.telegram.states._id.add(cq.getFrom().getId());
                Const.telegram.states._state.add(Const.telegram.states.STD);
            }
            keyboardMenu.handler(cq.getData(),cq.getFrom().getId(),update.getCallbackQuery().getMessage());
        }
    }
    public void sendMsg(Long id, String content){
        SendMessage sm = SendMessage.builder().chatId(id.toString()).text(content).build();
        try {
            execute(sm);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    public void sendMsg(Long id, String content, InlineKeyboardMarkup inlineKeyboardMarkup){
        SendMessage sm = SendMessage.builder().chatId(id.toString()).text(content).build();
        sm.setReplyMarkup(inlineKeyboardMarkup);
        try {
            execute(sm);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    public void sendMsg(Long id, String content, String document) throws TelegramApiException{
        SendDocument msg = new SendDocument();
        msg.setChatId(id.toString());
        msg.setDocument(new InputFile(document));
        msg.setCaption(content);
        try {
            execute(msg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    public class Commands {
        KeyboardMenu keyboardMenu = new KeyboardMenu();
        public void handler(String command, Message msg)
        {
            switch (command)
            {
                case "/start":
                    start(msg.getFrom().getId());
                    break;

                case "/loadfile":
                    loadfile(msg.getFrom().getId());
                    break;
            }

        }
        void start(Long id)
        {
            Const.telegram.states._state.set(state_find(id), Const.telegram.states.STD);
            sendMsg(id,Const.telegram.msg_strings.START_TEXT,keyboardMenu.kb_start());

        }

        void loadfile(Long id)
        {
            Const.telegram.states._state.set(state_find(id), Const.telegram.states.FILELOAD_FILE);
            sendMsg(id,Const.telegram.msg_strings.FILELOAD_START);
        }
    }

    public class KeyboardMenu{
        public InlineKeyboardMarkup kb_start()
        {
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            List<InlineKeyboardButton> buttonRow = new ArrayList<>();
            List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

            buttonRow.add(kb_setbuttons("Просмотреть пользовательские файлы","start_button"));
            rowList.add(buttonRow);

            inlineKeyboardMarkup.setKeyboard(rowList);
            return inlineKeyboardMarkup;
        }

        public InlineKeyboardButton kb_setbuttons(String text, String CallbackData)
        {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(text);
            button.setCallbackData(CallbackData);
            return  button;
        }

        public InlineKeyboardMarkup kb_setbtnsubjectload(int _class)
        {
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
            for(String i : Const.telegram.msg_strings.subjects[_class-7])
            {
                List<InlineKeyboardButton> buttonRow = new ArrayList<>();
                buttonRow.add(kb_setbuttons(i,  "subjbutton" + i));
                rowList.add(buttonRow);
            }
            inlineKeyboardMarkup.setKeyboard(rowList);
            return inlineKeyboardMarkup;
        }
        public InlineKeyboardMarkup kb_subjectsave(long id)
        {
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
             for(String i : Const.telegram.msg_strings.subjects[Const.telegram.states._state.get(state_find(id))-7])
            {
                List<InlineKeyboardButton> buttonRow = new ArrayList<>();
                buttonRow.add(kb_setbuttons(i,  "subjectsave" + i));
                rowList.add(buttonRow);
            }
            inlineKeyboardMarkup.setKeyboard(rowList);
            return inlineKeyboardMarkup;
        }

        public InlineKeyboardMarkup kb_filesbuttonsset()
        {

            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
            for(int i = 7; i <= 11; i++) {
                List<InlineKeyboardButton> buttonRow = new ArrayList<>();
                buttonRow.add(kb_setbuttons(Integer.toString(i), "classsave" + i));
                rowList.add(buttonRow);

            }
            inlineKeyboardMarkup.setKeyboard(rowList);
            return inlineKeyboardMarkup;
            /*
            db_connect.db_connect();
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
            int n = 1;
            for(String i : db_connect.filedb_getcolumn(Const.database.userfile.DOCNAME)) {
                List<InlineKeyboardButton> buttonRow = new ArrayList<>();
                buttonRow.add(kb_setbuttons(i, "file_button" + n));
                rowList.add(buttonRow);
                n++;
            }
            inlineKeyboardMarkup.setKeyboard(rowList);
            db_connect.db_close();
            return inlineKeyboardMarkup;
            */
        }

        public InlineKeyboardMarkup kb_setbtnclassload()
        {
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
            for(int i = 7; i <= 11; i++)
            {
                List<InlineKeyboardButton> buttonRow = new ArrayList<>();
                buttonRow.add(kb_setbuttons(Integer.toString(i), "classselect_button" + i));
                rowList.add(buttonRow);
            }
            inlineKeyboardMarkup.setKeyboard(rowList);
            return inlineKeyboardMarkup;
        }

        public void handler(String callback, Long id, Message msg)
        {
            switch (callback)
            {
                case "start_button":
                    db_connect.db_connect();
                    EditMessageText edm = new EditMessageText();
                    edm.setText("Выберете предмет:");
                    edm.setReplyMarkup(kb_filesbuttonsset());
                    edm.setMessageId(msg.getMessageId());
                    edm.setChatId(msg.getChatId().toString());
                    try {
                        execute(edm);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    db_connect.db_close();
                    break;
            }
            if(callback.contains("file_button"))
            {
                int temp = Integer.parseInt(callback.replace("file_button",""));
                try {
                    db_connect.db_connect();
                    String content = "by: @" + db_connect.filedb_findbyid(temp, Const.database.userfile.FROMUSER)
                            +"\nfile: " + db_connect.filedb_findbyid(temp, Const.database.userfile.DOCNAME)
                            +"\nclass: " + db_connect.filedb_findbyid(temp, Const.database.userfile.DOCCLASS)
                            +"\nsubject: " + db_connect.filedb_findbyid(temp, Const.database.userfile.DOCSUBJECT);
                    EditMessageText edm = new EditMessageText();
                    edm.setText("Ваш файл:");
                    edm.setMessageId(msg.getMessageId());
                    edm.setChatId(msg.getChatId().toString());
                    try {
                        execute(edm);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    sendMsg(id,content, db_connect.filedb_findbyid(temp,Const.database.userfile.DOCID));
                    db_connect.db_close();
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                Const.telegram.states._state.set(state_find(id),Const.telegram.states.STD);
            }
            else if(callback.contains("classselect_button"))
            {
                int temp = Integer.parseInt(callback.replace("classselect_button",""));
                db_connect.db_connect();
                db_connect.filedb_update(db_connect.db_lastid("SELECT ID FROM " + Const.database.userfile.NAME),temp,Const.database.userfile.DOCCLASS);
                Const.telegram.states._state.set(state_find(id),Const.telegram.states.FILELOAD_SUBJECT);
                db_connect.db_close();
                EditMessageText edm = new EditMessageText();
                edm.setText("Выберете предмет:");
                edm.setReplyMarkup(kb_setbtnsubjectload(temp));
                edm.setMessageId(msg.getMessageId());
                edm.setChatId(msg.getChatId().toString());
                try {
                    execute(edm);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
            else if(callback.contains("subjbutton"))
            {
                String temp = callback.replace("subjbutton","");
                db_connect.db_connect();
                db_connect.filedb_update(db_connect.db_lastid("SELECT ID FROM " + Const.database.userfile.NAME),temp,Const.database.userfile.DOCSUBJECT);
                Const.telegram.states._state.set(state_find(id),Const.telegram.states.STD);
                db_connect.db_close();
                EditMessageText edm = new EditMessageText();
                edm.setText("Файл успешно загружен");
                edm.setMessageId(msg.getMessageId());
                edm.setChatId(msg.getChatId().toString());
                try {
                    execute(edm);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
            else if(callback.contains("classsave")) {
                int temp = Integer.parseInt(callback.replace("classsave", ""));
                Const.telegram.states._state.set(state_find(id),Const.telegram.states.CLASS_SELECTED[temp-7]);
                EditMessageText edm = new EditMessageText();
                edm.setText("Выберете предмет:");
                edm.setReplyMarkup(kb_subjectsave(msg.getChatId()));
                edm.setMessageId(msg.getMessageId());
                edm.setChatId(msg.getChatId().toString());
                try {
                    execute(edm);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
            else if(callback.contains("subjectsave")) {
                String temp = callback.replace("subjectsave", "");
                db_connect.db_connect();
                InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
                int n = 1;


                for (String j : db_connect.filedb_getcolumnS(Const.database.userfile.DOCSUBJECT)) {

                    if (j.equals(temp) && (Integer.parseInt(db_connect.filedb_findbyid(n,
                            Const.database.userfile.DOCCLASS)) == (Const.telegram.states._state.get(state_find(id))))) {
                        List<InlineKeyboardButton> buttonRow = new ArrayList<>();

                        buttonRow.add(kb_setbuttons(db_connect.filedb_findbyid(n, Const.database.userfile.DOCNAME),
                                "file_button" + n));
                        rowList.add(buttonRow);
                    }
                    n++;
                }


                inlineKeyboardMarkup.setKeyboard(rowList);
                EditMessageText edm = new EditMessageText();
                edm.setText("Выберете файл:");
                edm.setReplyMarkup(inlineKeyboardMarkup);
                edm.setMessageId(msg.getMessageId());
                edm.setChatId(msg.getChatId().toString());
                try {
                    execute(edm);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
                db_connect.db_close();
                Const.telegram.states._state.set(state_find(id),Const.telegram.states.STD);

            }
        }
    }

    public Integer state_find(Long id)
    {
        Integer _id = null;
        for(Long i : Const.telegram.states._id)
        {
            if (i.compareTo(id) == 0)
            {
                _id = Const.telegram.states._id.indexOf(i);
            }
        }

        return _id;
    }
}