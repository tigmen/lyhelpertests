package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class db_Connect{
    Connection connection;
    Statement statement;
    public void db_connect() {
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
    public void db_close()
    {
        try {
            connection.close();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void db_drop()
    {
        try {
            statement.execute(Const.database.userdata_table.DROP);
            statement.execute(Const.database.userfile.DROP);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

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
    public String filedb_findbyid(int _id, String what)
    {
        String result = new String();
        try {
            ResultSet rs = statement.executeQuery(Const.database.userfile.SELECT);
            while (rs.next()) {
                if(rs.getInt(Const.database.userfile.ID) == _id)
                result = rs.getString(what);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }
    public void filedb_selectAll(){
        try {
            ResultSet rs = statement.executeQuery(Const.database.userfile.SELECT);
            while (rs.next())
            {
                System.out.println(rs.getInt(Const.database.userfile.ID)+ "\t" +
                        rs.getLong(Const.database.userfile.FROMUSEID) +"\t"+
                        rs.getString(Const.database.userfile.FROMUSER) +"\t"+
                        rs.getString(Const.database.userfile.DOCID));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void filedb_update(int id, String name,String what)
    {
        try {
            PreparedStatement prst = connection.prepareStatement("UPDATE " + Const.database.userfile.NAME + " SET "
                    + what + " = ?" + " WHERE " +Const.database.userfile.ID+" = ?");
            prst.setString(1,name);
            prst.setInt(2,id);

            prst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void filedb_update(int id, int name,String what)
    {
        try {
            PreparedStatement prst = connection.prepareStatement("UPDATE " + Const.database.userfile.NAME + " SET "
                    + what + " = ?" + " WHERE " +Const.database.userfile.ID+" = ?");
            prst.setInt(1,name);
            prst.setInt(2,id);

            prst.executeUpdate();
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

    public List<String> filedb_getcolumnS(String columnid)
    {
        List<String> result = new ArrayList<>();
        try {
            ResultSet rs = statement.executeQuery("SELECT "+ columnid +" FROM " + Const.database.userfile.NAME);
            while (rs.next()) result.add(rs.getString(columnid));
            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;

    }
    public List<Integer> filedb_getcolumn(String columnid)
    {
        List<Integer> result = new ArrayList<>();
        try {
            ResultSet rs = statement.executeQuery("SELECT "+ columnid +" FROM " + Const.database.userfile.NAME);
            while (rs.next()) result.add(rs.getInt(columnid));
            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;

    }
    public void db_insert(Long id, String username)
    {
        boolean id_repeated = false;
        try {
            ResultSet rs = statement.executeQuery(Const.database.userdata_table.SELECT);
            while (rs.next())
            {
                if(id.compareTo((Long)rs.getLong(Const.database.userdata_table.USER_ID))==0) id_repeated = true;
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
    public void filedb_insert(Long id, String username, String docid, String docname)
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
}