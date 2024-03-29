package org.example;

import java.util.ArrayList;
import java.util.List;

public class Const {
    //telegram settings
    public static class telegram {
        public static final String USERNAME = "@lyceumhelperbot";
        public static final String TOKEN = "5847776986:AAGrBIagucIVSvYi84xo7hqgMrQGLFZPq7I";
        public class msg_strings
        {
            public static final String START_TEXT = "Вас приветствует lyceumhelper, свободная площадка с обучающими материалами";
            public static final String FILELOAD_START = "Загрузите файл";
            public static final String FILELOAD_ERROR_NOFILE = "Прикрепите файл к сообщению, учтите, что название файла должно отражать содержимое файла";
            public static final String FILELOAD_LOADED = "Файл успешно загружен(наверное)";

            public static final String[][] subjects = {
                    {"Математика","Физика","История","Информатика","Русский язык","Обществознание","География","Биология"},
                    {"Математика","Физика","История","Информатика","Русский язык","Обществознание","География","Биология","ОБЖ","Химия"},
                    {"Математика","Физика","История","Информатика","Русский язык","Обществознание","География","Биология","ОБЖ","Химия"},
                    {"Математика","Физика","История","Информатика","Русский язык","Обществознание","География","Биология","ОБЖ","Химия"},
                    {"Математика","Физика","История","Информатика","Русский язык","Обществознание","География","Биология","ОБЖ","Химия"}
            };

            public static final String[][] timetablesurl = {
                    {
                            "https://1502.mskobr.ru/files/rasp/alpha/7%D0%93.pdf",
                            "https://1502.mskobr.ru/files/rasp/alpha/7%D0%94.pdf",
                            "https://1502.mskobr.ru/files/rasp/alpha/7%D0%95.pdf",
                            "https://1502.mskobr.ru/files/rasp/alpha/7%D0%96.pdf",
                            "https://1502.mskobr.ru/files/rasp/alpha/7%D0%97.pdf",
                    }, {
                            "https://1502.mskobr.ru/files/rasp/alpha/8%D0%93.pdf",
                            "https://1502.mskobr.ru/files/rasp/alpha/8%D0%94.pdf",
                            "https://1502.mskobr.ru/files/rasp/alpha/8%D0%95.pdf",
                            "https://1502.mskobr.ru/files/rasp/alpha/8%D0%96.pdf",
                            "https://1502.mskobr.ru/files/rasp/alpha/8%D0%97.pdf",
                            "https://1502.mskobr.ru/files/rasp/alpha/8%D0%98.pdf",
                            "https://1502.mskobr.ru/files/rasp/alpha/8%D0%99.pdf",
                            "https://1502.mskobr.ru/files/rasp/alpha/8%D0%A7.pdf",
                            "https://1502.mskobr.ru/files/rasp/alpha/8%D0%A8.pdf",
                    }, {
                            "https://1502.mskobr.ru/files/rasp/alpha/9%D0%92.pdf",
                            "https://1502.mskobr.ru/files/rasp/alpha/9%D0%93.pdf",
                            "https://1502.mskobr.ru/files/rasp/alpha/9%D0%94.pdf",
                            "https://1502.mskobr.ru/files/rasp/alpha/9%D0%95.pdf",
                            "https://1502.mskobr.ru/files/rasp/alpha/9%D0%96.pdf",
                            "https://1502.mskobr.ru/files/rasp/alpha/9%D0%97.pdf",
                            "https://1502.mskobr.ru/files/rasp/alpha/9%D0%98.pdf",
                    }, {
                            "https://1502.mskobr.ru/files/rasp/alpha/10%D0%93.pdf",
                            "https://1502.mskobr.ru/files/rasp/alpha/10%D0%94.pdf",
                            "https://1502.mskobr.ru/files/rasp/alpha/10%D0%95.pdf",
                            "https://1502.mskobr.ru/files/rasp/alpha/10%D0%96.pdf",
                            "https://1502.mskobr.ru/files/rasp/alpha/10%D0%97.pdf",
                            "https://1502.mskobr.ru/files/rasp/alpha/10%D0%98.pdf",
                            "https://1502.mskobr.ru/files/rasp/alpha/10%D0%9A.pdf",
                    }, {
                            "https://1502.mskobr.ru/files/rasp/alpha/11%D0%93.pdf",
                            "https://1502.mskobr.ru/files/rasp/alpha/11%D0%94.pdf",
                            "https://1502.mskobr.ru/files/rasp/alpha/11%D0%95.pdf",
                            "https://1502.mskobr.ru/files/rasp/alpha/11%D0%96.pdf",
                            "https://1502.mskobr.ru/files/rasp/alpha/11%D0%97.pdf",
                            "https://1502.mskobr.ru/files/rasp/alpha/11%D0%98.pdf",
                            "https://1502.mskobr.ru/files/rasp/alpha/11%D0%9A.pdf"
                    }
            };

        public static final String[][] timetablesindex = {
                {"Г","Д","Е","Ж","З"}, {"Г", "Д", "Е", "Ж", "З", "И", "Ф", "Ч", "Ш",}, {"В", "Г", "Д", "Е", "Ж", "З", "И",}, {"Г", "Д", "Е", "Ж", "З", "И", "К",}, {"Г", "Д", "Е", "Ж", "З", "И", "К",}
        };
    }
        public static class states
        {

            public static List<Long> _id = new ArrayList<>();
            public static List<Integer> _state = new ArrayList<>();

            public static final int STD = 0;
            public static final int FILELOAD_FILE = 1;
            public static final int FILELOAD_NAME = 2;
            public static final int FILELOAD_CLASS = 3;
            public static final int FILELOAD_SUBJECT = 4;
            public static final int FILELOAD_TOPIC = 5; // потом реализую

            public static final int WAITING_SEARCH = 6;

            public static final int CLASS_SELECTED[] = {7,8,9,10,11};
            public static final int CLASS_TIMETABLE[] = {7,8,9,10,11};
        }
    }
    //db settings
    public static class database {
        public static final String BASENAME = "std.db";
        public static final String PATH = "";

        public class userdata_table
        {
            public static final String NAME = "USERDATA";
            public static final String ID = "ID";
            public static final String USER_ID = "USERID";
            public static final String USER_NAME = "USERDATA";


            public static final String STRUCTURE = "CREATE TABLE IF NOT EXISTS " + NAME + " (" + ID
                    + " INTEGER PRIMARY KEY," + USER_ID + " BIGINT," + USER_NAME + " TEXT);";
            public static final String DROP = "DROP TABLE IF EXISTS " + NAME;
            public static final String SELECT = "SELECT " +ID +","+USER_ID+","+USER_NAME+" FROM " + NAME;
            public static final String INSERT = "INSERT INTO " + NAME + "("+USER_ID+","+USER_NAME+") VALUES(?,?)";
        }

        public class userfile
        {
            public static final String NAME = "USERFILES";
            public static final String ID = "ID";
            public static final String FROMUSER = "FROMUSER";
            public static final String FROMUSEID = "FROMUSERID";
            public static final String DOCID = "DOCID";
            public static final String DOCNAME = "DOCNAME";
            public static final String DOCCLASS = "DOCCLASS";
            public static final String DOCSUBJECT = "DOCSUBJECT";
            public static final String DOCTOPIC = "DOCTOPIC";
            public static final String STRUCTURE = "CREATE TABLE IF NOT EXISTS " + NAME + " (" + ID
                    + " INTEGER PRIMARY KEY," + FROMUSEID + " BIGINT," + FROMUSER + " TEXT," +DOCID+" TEXT,"+DOCNAME+" TEXT," +
                     DOCCLASS + " INT,"+ DOCSUBJECT + " TEXT,"+ DOCTOPIC + " TEXT);";
            public static final String DROP = "DROP TABLE IF EXISTS " + NAME;
            public static final String SELECT = "SELECT " +ID +","+FROMUSEID+","+FROMUSER+","+DOCID
                    +","+DOCNAME+","+DOCCLASS+","+DOCSUBJECT+","+DOCTOPIC+" FROM " + NAME;
            public static final String INSERT = "INSERT INTO " + NAME + "("+FROMUSEID+","+FROMUSER+","+DOCID+","+DOCNAME+","+DOCCLASS+","+DOCSUBJECT+","+DOCTOPIC+") VALUES(?,?,?,?,?,?,?)";
        }
        //мб потом использую
        /*
        public class userstates
        {
            public static final String NAME = "USERSTATES";

            public static final String ID = "ID";
            public  static  final String USERID = "USERID";
            public final static String STATE = "USERSTATE";


            public static final String STRUCTURE = "CREATE TABLE IF NOT EXISTS " + NAME + " (" + ID
                    + " INTEGER PRIMARY KEY," + USERID + " BIGINT," +STATE+" TEXT);";
            public static final String DROP = "DROP TABLE IF EXISTS " + NAME;
            public static final String SELECT = "SELECT " + ID + "," + USERID + ","+ STATE +" FROM " + NAME;
            public static final String INSERT = "INSERT INTO " + NAME + "("+ USERID +","+STATE+") VALUES(?,?)";
        }
        */

    }
}
