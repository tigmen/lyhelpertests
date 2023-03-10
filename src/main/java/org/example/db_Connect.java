package org.example;

import org.checkerframework.checker.units.qual.A;

import java.beans.Customizer;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class db_Connect{
    Connection connection;
    Statement statement;
//    Подключение к базе данных через jdbc
//    вставлять перед любым использованием методов с учатием db
    private void db_connect() {
        try
        {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:"+ Const.database.PATH + Const.database.BASENAME);
            statement = connection.createStatement();
            statement.execute(Const.database.userdata_table.STRUCTURE);
            statement.execute(Const.database.userfile.STRUCTURE);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
//    Метод для закрытия базы данных
//    использовать после любого использования методов с db (то есть открыл - выполнил нужные методы - закрыл)
    private void db_close()
    {
        try {
            connection.close();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
//    функция для подулючения к дб, подключение выглядит примерно так:
    /*public void example()
    {
        db_Connect db_connect = new db_Connect();
        db_connect.db_execute(c ->{
            <логика>
        });
    }*/
//    таким образом думать о открытие закрытие подключения ради экономии ресурсов не нужно)
    public void db_execute(Consumer<db_Connect> consumer)
    {
        this.db_connect();
        consumer.accept(this);
        this.db_close();
    }

//    db_drop() сбрасывает данные все таблиц, обнуляет базу данных короче говоря
//    если подать на вход строку, название таблицы, сбросит только эту таблицу
    public void db_drop()
    {
        try {
            statement.execute(Const.database.userdata_table.DROP);
            statement.execute(Const.database.userfile.DROP);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void db_drop(String db_name)
    {
        try {
            switch (db_name) {
                case Const.database.userfile.NAME -> statement.execute(Const.database.userfile.DROP);
                case Const.database.userdata_table.NAME -> statement.execute(Const.database.userdata_table.DROP);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
//    находит последний элемент таблицы путем перебора всех элементов
//    рационально ли это? нихуя подобного
//    в качестве входных аргументов принимает строку вида "SELECT a,b,c FROM table_name"
    public int db_lastid(String table_select)
    {
        int result = 0;
        try {
            ResultSet rs = statement.executeQuery(table_select);
            while (rs.next())
            {
                result = rs.getInt(Const.database.userfile.ID);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
//    добавляет новый элемент в таблицу(в зависимости от количества входных параметров в разные таблицы)
    public void db_insert(Long id, String username)
    {
        boolean id_repeated = false;
        try {
            ResultSet rs = statement.executeQuery(Const.database.userdata_table.SELECT);
            while (rs.next())
            {
                if(id.compareTo(rs.getLong(Const.database.userdata_table.USER_ID))==0) id_repeated = true;
            }
            if(!id_repeated) {
                PreparedStatement prst = connection.prepareStatement(Const.database.userdata_table.INSERT);
                prst.setLong(1, id);
                prst.setString(2, username);
                prst.executeUpdate();
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void db_insert(Long id, String username, String docid, String docname)
    {
        try {

            ResultSet rs = statement.executeQuery(Const.database.userfile.SELECT);
            PreparedStatement prst = connection.prepareStatement(Const.database.userfile.INSERT);
            prst.setLong(1, id);
            prst.setString(2, username);
            prst.setString(3, docid);
            prst.setString(4, docname);
            prst.setInt(5,-1);
            prst.setString(6,"temp");
            prst.setString(7,"temp");
            prst.executeUpdate();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
//    методы db_getcolumn получают данные из определенного столбца columnid таблицы table_name
    public List<String> db_getcolumnS(String table_name,String columnid)
    {
        List<String> result = new ArrayList<>();
                try {
                    switch (table_name) {
                        case Const.database.userfile.NAME -> {
                            ResultSet rs = statement.executeQuery("SELECT " + columnid + " FROM " + Const.database.userfile.NAME);
                            while (rs.next()) result.add(rs.getString(columnid));
                            rs.close();
                        }
                        case Const.database.userdata_table.NAME -> {
                            ResultSet _rs = statement.executeQuery("SELECT " + columnid + " FROM " + Const.database.userdata_table.NAME);
                            while (_rs.next()) result.add(_rs.getString(columnid));
                            _rs.close();
                        }
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
        return result;

    }
    public List<Integer> db_getcolumnI(String table_name, String columnid)
    {
        List<Integer> result = new ArrayList<>();
        try {
            switch (table_name) {
                case Const.database.userfile.NAME -> {
                    ResultSet rs = statement.executeQuery("SELECT " + columnid + " FROM " + Const.database.userfile.NAME);
                    while (rs.next()) result.add(rs.getInt(columnid));
                    rs.close();
                }
                case Const.database.userdata_table.NAME -> {
                    ResultSet _rs = statement.executeQuery("SELECT " + columnid + " FROM " + Const.database.userdata_table.NAME);
                    while (_rs.next()) result.add(_rs.getInt(columnid));
                    _rs.close();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;

//   Ищет элемент по _id в столбце what таблицы table_name
    }
    public String db_findbyid(String table_name,int _id, String what) {
        String result = "";
        try {
            switch (table_name) {
                case Const.database.userfile.NAME -> {

                    ResultSet rs = statement.executeQuery(Const.database.userfile.SELECT);
                    while (rs.next()) {
                        if (rs.getInt(Const.database.userfile.ID) == _id)
                            result = rs.getString(what);
                    }
                    rs.close();
                }
                case Const.database.userdata_table.NAME -> {
                    ResultSet _rs = statement.executeQuery(Const.database.userdata_table.SELECT);
                    while (_rs.next()) {
                        if (_rs.getInt(Const.database.userfile.ID) == _id)
                            result = _rs.getString(what);
                    }
                    _rs.close();
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
            return result;
        }
    public String db_findbyid(String table_name,String where_id,String _id, String what) {
        String result = "";
        try {
            switch (table_name) {
                case Const.database.userfile.NAME -> {

                    ResultSet rs = statement.executeQuery(Const.database.userfile.SELECT);
                    while (rs.next()) {
                        if (rs.getString(where_id).equals(_id))
                            result = rs.getString(what);
                    }
                    rs.close();
                }
                case Const.database.userdata_table.NAME -> {
                    ResultSet _rs = statement.executeQuery(Const.database.userdata_table.SELECT);
                    while (_rs.next()) {
                        if (_rs.getString(where_id).equals(_id))
                            result = _rs.getString(what);
                    }
                    _rs.close();
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    public String db_findbyid(String table_name,String where_id,int _id, String what) {
        String result = "";
        try {
            switch (table_name) {
                case Const.database.userfile.NAME -> {

                    ResultSet rs = statement.executeQuery(Const.database.userfile.SELECT);
                    while (rs.next()) {
                        if (rs.getInt(where_id) == _id)
                            result = rs.getString(what);
                    }
                    rs.close();
                }
                case Const.database.userdata_table.NAME -> {
                    ResultSet _rs = statement.executeQuery(Const.database.userdata_table.SELECT);
                    while (_rs.next()) {
                        if (_rs.getInt(where_id) == _id)
                            result = _rs.getString(what);
                    }
                    _rs.close();
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
//        метод изменяет значение строчки line_id=id в столбце columnid таблицы table_name на content
    public void db_update(String table_name, String columnid, String line_id,Integer id, String content)
    {
        try {
            PreparedStatement prst = connection.prepareStatement("UPDATE "+table_name+" SET "+columnid+" = ? WHERE "+line_id+" = ?");
            prst.setString(1,content);
            prst.setInt(2,id);
            prst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void db_update(String table_name, String columnid, String line_id,Integer id, Integer content)
    {
        try {
            PreparedStatement prst = connection.prepareStatement("UPDATE "+table_name+" SET "+columnid+" = ? WHERE "+line_id+" = ?");
            prst.setInt(1,content);
            prst.setInt(2,id);
            prst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    алгоритм линейного поиска по подстроке
    public List<String> lineralsearch(String table_name,String columnid, String substring)
    {
        List<String> result = new ArrayList<>();
        List<String> from = db_getcolumnS(table_name, columnid);
        from.forEach(c ->{
            if(c.toLowerCase().contains(substring)) result.add(c);
        });
        return result;
    }
//    далее идет легаси, который я использовать настоятельно не рекомендую!
//    не иди сюда, это ситуативная хуйня, не иди
    public void db_selectAll(){
        try {
            ResultSet rs = statement.executeQuery(Const.database.userdata_table.SELECT);
            while (rs.next())
            {
                System.out.println(rs.getInt(Const.database.userdata_table.ID)+ "\t" +
                        rs.getLong(Const.database.userdata_table.USER_ID) +"\t"+
                        rs.getString(Const.database.userdata_table.USER_NAME));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void filedb_selectAll(){
        try {
            ResultSet rs = statement.executeQuery(Const.database.userfile.SELECT);
            while (rs.next())
            {
                System.out.println(rs.getInt(Const.database.userfile.ID)+ "\t" +
                        rs.getLong(Const.database.userfile.FROMUSEID) +"\t"+
                        rs.getString(Const.database.userfile.FROMUSER) +"\t"+
                        rs.getString(Const.database.userfile.DOCID) +"\t"+
                        rs.getString(Const.database.userfile.DOCNAME) +"\t"+
                        rs.getInt(Const.database.userfile.DOCCLASS) +"\t"+
                        rs.getString(Const.database.userfile.DOCSUBJECT) +"\t"+
                        rs.getString(Const.database.userfile.DOCTOPIC));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public List<Long> db_getAllid()
    {
        List<Long> result = new ArrayList<>();
        try {
            ResultSet rs = statement.executeQuery(Const.database.userdata_table.SELECT);
            while (rs.next())
            {
                result.add(rs.getLong(Const.database.userdata_table.USER_ID));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }




}
