package org.example;
import org.checkerframework.checker.units.qual.C;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;


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
            if(!Const.telegram.states._id.contains(msg.getFrom().getId()))
            {
                Const.telegram.states._id.add(msg.getFrom().getId());
                Const.telegram.states._state.add(Const.telegram.states.STD);
            }
            switch (Const.telegram.states._state.get(state_find(msg.getFrom().getId()))) {
                case Const.telegram.states.STD -> {
                    db_connect.db_execute(c -> c.db_insert(msg.getFrom().getId(), msg.getFrom().getUserName()));
                    db_connect.db_execute(c -> db_connect.lineralsearch(Const.database.userfile.NAME, Const.database.userfile.DOCNAME, "Коллоквиум"));
                    if (msg.isCommand()) {
                        commands.handler(msg.getText(), msg);
                    } else if (update.hasCallbackQuery()) {
                        var callback = update.getCallbackQuery();
                        keyboardMenu.handler(callback.getData(), callback.getFrom().getId(), callback.getMessage());
                    }
                }
                case Const.telegram.states.FILELOAD_FILE -> {
                    if (msg.hasDocument()) {
                        db_connect.db_execute(c -> c.db_insert(msg.getFrom().getId(), msg.getFrom().getUserName(),
                                msg.getDocument().getFileId(), msg.getDocument().getFileName()));

                        Const.telegram.states._state.set(state_find(msg.getFrom().getId()), Const.telegram.states.FILELOAD_NAME);
                        sendMsg(msg.getFrom().getId(), "Введите название конспекта: ");
                    } else {
                        sendMsg(msg.getFrom().getId(), Const.telegram.msg_strings.FILELOAD_ERROR_NOFILE);
                    }
                }
                case Const.telegram.states.FILELOAD_NAME -> {
                    db_connect.db_execute(c -> c.db_update(Const.database.userfile.NAME, Const.database.userfile.DOCNAME, Const.database.userfile.ID,
                                    c.db_lastid("SELECT ID FROM " + Const.database.userfile.NAME), msg.getText()));
                    Const.telegram.states._state.set(state_find(msg.getFrom().getId()), Const.telegram.states.FILELOAD_CLASS);
                        sendMsg(msg.getFrom().getId(), "Материал какого это класса?", new InlineKeyboardMarkup(keyboardMenu.kb_generator(Arrays.asList(7,8,9,10,11),"classselect_button",null)));

                }
                case Const.telegram.states.FILELOAD_CLASS -> sendMsg(msg.getFrom().getId(), "Выберите класс");
                case Const.telegram.states.WAITING_SEARCH ->
                {
                    Const.telegram.states._state.set(state_find(msg.getFrom().getId()),Const.telegram.states.STD);

                    db_connect.db_execute(c -> {
                        KeyboardMenu km = new KeyboardMenu();
                        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
                        int n = 1;
                        for (String i : c.lineralsearch(Const.database.userfile.NAME,Const.database.userfile.DOCNAME,msg.getText().toLowerCase())) {
                                List<InlineKeyboardButton> buttonRow = new ArrayList<>();
                                buttonRow.add(km.kb_setbuttons(i, "file_button"
                                        + c.db_findbyid(Const.database.userfile.NAME,Const.database.userfile.DOCNAME, i, Const.database.userfile.ID)));
                                rowList.add(buttonRow);
                                n++;
                        }
                        inlineKeyboardMarkup.setKeyboard(rowList);
                        sendMsg(msg.getChatId(), "Выберите файл:",inlineKeyboardMarkup);
                        Const.telegram.states._state.set(state_find(msg.getChatId()), Const.telegram.states.STD);
                    });
                }
            }
        }
        else if(update.hasCallbackQuery())
        {
            var cq = update.getCallbackQuery();
            if(!Const.telegram.states._id.contains(cq.getFrom().getId()))
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

    public void editMsg(Message msg, String content, InlineKeyboardMarkup inlineKeyboardMarkup){
        EditMessageText edm = new EditMessageText();
        edm.setText(content);
        edm.setMessageId(msg.getMessageId());
        edm.setChatId(msg.getChatId().toString());
        edm.setReplyMarkup(inlineKeyboardMarkup);
        try {
            execute(edm);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void editMsg(Message msg, String content){
        EditMessageText edm = new EditMessageText();
        edm.setText(content);
        edm.setMessageId(msg.getMessageId());
        edm.setChatId(msg.getChatId().toString());
        try {
            execute(edm);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }



    public class Commands {
        KeyboardMenu keyboardMenu = new KeyboardMenu();
        public void handler(String command, Message msg)
        {
            switch (command) {
                case "/start" -> start(msg.getFrom().getId());
                case "/loadfile" -> loadfile(msg.getFrom().getId());
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
         interface kb_std<T>
        {
            public List<InlineKeyboardButton> accept(String arg1, T arg2);
        }
        public <T> List<List<InlineKeyboardButton>> kb_generator(List<T> list,String arg, kb_std<T> std)
        {
            List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
            switch (std) {
                case null -> list.forEach(c ->{
                    List<InlineKeyboardButton> buttonRow = new ArrayList<>();
                    buttonRow.add(kb_setbuttons(c.toString(),  arg + c));
                    rowList.add(buttonRow);
                });
                default -> list.forEach(c -> rowList.add(std.accept(arg,c)));
            }
            return rowList;
        }


        public void handler(String callback, Long id, Message msg)
        {
            if (callback.equals("start_button")) {

                List<List<InlineKeyboardButton>> buttons = kb_generator(Arrays.asList(7,8,9,10,11),"classsave", (arg, c) ->{
                    List<InlineKeyboardButton> buttonRow = new ArrayList<>();
                    buttonRow.add(kb_setbuttons(Integer.toString(c), arg + c));
                    return  buttonRow;
                });
                buttons.add(Arrays.asList(kb_setbuttons("Поиск", "search")));
                editMsg(msg, "Выберете предмет:", new InlineKeyboardMarkup(buttons));
        }
            else if(callback.equals("search"))
            {
                editMsg(msg,"Введите название или часть названия конспекта, который ищите:");
                Const.telegram.states._state.set(state_find(id),Const.telegram.states.WAITING_SEARCH);
            }
            else if (callback.equals("classsave")) {
            }
            else if(callback.contains("file_button"))
            {
                int temp = Integer.parseInt(callback.replace("file_button",""));
                    db_connect.db_execute(c -> {String content = "by: @" + c.db_findbyid(Const.database.userfile.NAME,temp, Const.database.userfile.FROMUSER)
                            +"\nfile: " + c.db_findbyid(Const.database.userfile.NAME,temp, Const.database.userfile.DOCNAME)
                            +"\nclass: " + c.db_findbyid(Const.database.userfile.NAME,temp, Const.database.userfile.DOCCLASS)
                            +"\nsubject: " + c.db_findbyid(Const.database.userfile.NAME,temp, Const.database.userfile.DOCSUBJECT);

                        editMsg(msg,"Ваш файл:");
                    try {
                        sendMsg(id,content, c.db_findbyid(Const.database.userfile.NAME,temp,Const.database.userfile.DOCID));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }

            });
                Const.telegram.states._state.set(state_find(id),Const.telegram.states.STD);
            }
            else if(callback.contains("classselect_button"))
            {
                int temp = Integer.parseInt(callback.replace("classselect_button",""));
                db_connect.db_execute(c -> c.db_update(Const.database.userfile.NAME,Const.database.userfile.DOCCLASS,Const.database.userfile.ID,
                        c.db_lastid("SELECT ID FROM " + Const.database.userfile.NAME),temp));
                Const.telegram.states._state.set(state_find(id),Const.telegram.states.FILELOAD_SUBJECT);
                editMsg(msg, "Выберите предмет:",new InlineKeyboardMarkup(kb_generator(Arrays.asList(Const.telegram.msg_strings.subjects[temp-7]),"subjbutton",(arg,c) -> {
                            List<InlineKeyboardButton> buttonRow = new ArrayList<>();
                            buttonRow.add(kb_setbuttons(c.toString(), arg + c));
                            return buttonRow;
                        })));
            }
            else if(callback.contains("subjbutton"))
            {
                String temp = callback.replace("subjbutton","");
                db_connect.db_execute(c -> c.db_update(Const.database.userfile.NAME,Const.database.userfile.DOCSUBJECT,Const.database.userfile.ID,
                        c.db_lastid("SELECT ID FROM " + Const.database.userfile.NAME),temp));
                Const.telegram.states._state.set(state_find(id),Const.telegram.states.STD);
                editMsg(msg, "Файл успешно загружен");
            }
            else if(callback.contains("classsave")) {
                int temp = Integer.parseInt(callback.replace("classsave", ""));
                Const.telegram.states._state.set(state_find(id),Const.telegram.states.CLASS_SELECTED[temp-7]);
                editMsg(msg, "Выберите предмет:",new InlineKeyboardMarkup(kb_generator(Arrays.asList(Const.telegram.msg_strings.subjects[Const.telegram.states._state.get(state_find(id))-7]),"subjectsave",null)));
            }
            else if(callback.contains("subjectsave")) {
                String temp = callback.replace("subjectsave", "");
                InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
                db_connect.db_execute(c -> {
                    int n = 1;
                    for (String j : c.db_getcolumnS(Const.database.userfile.NAME, Const.database.userfile.DOCSUBJECT)) {

                        if (j.equals(temp) && (Integer.parseInt(c.db_findbyid(Const.database.userfile.NAME, n,
                                Const.database.userfile.DOCCLASS)) == (Const.telegram.states._state.get(state_find(id))))) {
                            List<InlineKeyboardButton> buttonRow = new ArrayList<>();

                            buttonRow.add(kb_setbuttons(c.db_findbyid(Const.database.userfile.NAME, n, Const.database.userfile.DOCNAME),
                                    "file_button" + n));
                            rowList.add(buttonRow);
                        }
                        n++;
                    }
                    inlineKeyboardMarkup.setKeyboard(rowList);
                    editMsg(msg, "Выберите файл:",inlineKeyboardMarkup);
                    Const.telegram.states._state.set(state_find(id), Const.telegram.states.STD);
                });
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