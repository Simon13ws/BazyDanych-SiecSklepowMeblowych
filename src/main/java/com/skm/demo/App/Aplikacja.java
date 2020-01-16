package com.skm.demo.App;

import com.skm.demo.Entities.Entity;
import com.skm.demo.Entities.Etat;
import com.skm.demo.RowMappers.EtatRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class Aplikacja implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private static Connection con;

    public static LinkedHashMap<Integer, String> getKeys(String tabela, Aplikacja a, String keyType, int pkOrTable) throws SQLException {

        DatabaseMetaData md = con.getMetaData();
        LinkedHashMap<Integer, String> keys = new LinkedHashMap<Integer, String>();
        ResultSet rsKeys;
        if(keyType.equals("primary"))
            rsKeys = md.getPrimaryKeys(null,null,tabela);
        else
            rsKeys = md.getImportedKeys(null,null,tabela);
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM "+tabela);
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnsCount = rsmd.getColumnCount();
        while(rsKeys.next()) {

            for (int i = 1; i <= columnsCount; i++) {
                try {
                    String kName = new String();
                    System.out.println(rsKeys.getObject("PKTABLE_NAME").toString());
                    if (keyType.equals("primary")) {
                        kName = rsKeys.getString("COLUMN_NAME");
                    } else {
                        kName = rsKeys.getString("FKCOLUMN_NAME");
                    }
                    //System.out.println(rsmd.getColumnName(i));
                    if (rsmd.getColumnName(i).equals(kName) && !kName.isEmpty() && !keys.containsKey(i)) {
                        if(pkOrTable == 1)
                            kName = rsKeys.getString("PKTABLE_NAME");
                        else if(pkOrTable == 0 && keyType.equals("foreign"))
                            kName = rsKeys.getString("PKCOLUMN_NAME");
                        keys.put(i, kName);
                        break;
                    }
                }catch(Exception e)
                {e.printStackTrace();}
            }
        }
        return keys;
    }

    public static int getNextNumber(String tabela) throws SQLException{
        String sql = "SELECT AUTO_INCREMENT FROM INFORMATION_SCHEMA.TABLES \n" +
                "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = '" + tabela + "'";
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(sql);
        rs.next();
        return rs.getInt("AUTO_INCREMENT");
    }

    public static ArrayList<String> selectColumn(String tabela, String column) throws SQLException {
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("SELECT " + column + " FROM " + tabela);

        ArrayList<String> values = new ArrayList<String>();
        int i = 0;
        while(rs.next())
        {
            values.add(rs.getObject(1).toString());
            System.out.println(i);
            i++;
        }

        return values;
    }

    public static ArrayList<String> getColumnNames(String tabela) throws SQLException {

        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM "+tabela + " LIMIT 1");
        ResultSetMetaData rsmd = rs.getMetaData();
        ArrayList<String> columnNames = new ArrayList<String>();
        int columnsCount = rsmd.getColumnCount();
        for(int i=1; i<=columnsCount; i++)
            columnNames.add(rsmd.getColumnName(i));
        return columnNames;
    }

    public static ArrayList<String[]> selectAll(String tabela) throws SQLException {

        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM " + tabela);

        int columnsCount = rs.getMetaData().getColumnCount();
        ArrayList<String[]> entities = new ArrayList<String[]>();
        while(rs.next())
        {
            String[] values = new String[columnsCount];
            for(int col=1; col<= columnsCount; col++)
            {
                Object value = rs.getObject(col);
                if(value != null)
                    values[col-1] = value.toString();
                else
                    values[col-1] = "";
            }
            entities.add(values);
        }

        return entities;
    }

    public static void deleteRow(String tabela, LinkedHashMap<String, Object> wartosci, Aplikacja a){
        String sql = "DELETE FROM " + tabela + " WHERE ";
        int n = 0;
        for(Map.Entry<String,Object> w: wartosci.entrySet()) {
            if(w.getKey().contains("id") || w.getKey().contains("numer"))
                sql += w.getKey() + " = " + w.getValue();
            else
                sql += w.getKey() + " = '" + w.getValue() + "'";
            if(n < wartosci.size()-1)
                sql += " AND ";
            else
                sql += ";";
        }
        JdbcTemplate jdbcTemplate2 = a.getJDBC();
        jdbcTemplate2.update(sql);
    }

    public JdbcTemplate getJDBC(){
        return jdbcTemplate;
    }


    //Dodawanie dziala ale jeszcze nie ma sprawdzania kluczy głównych i poprawności danych
    public static void addRow(String [] pola, String tabela, Aplikacja a) throws SQLException {
        JdbcTemplate jdbcTemplate2 = a.getJDBC();
        System.out.println("x");
        String sql = "INSERT INTO "+tabela+" (";
        ResultSetMetaData rsmd = podajMetaDaneTabeli(tabela);
        for(int i=0; i<rsmd.getColumnCount(); i++)
        {
            sql += rsmd.getColumnName(i+1);
            if(i<rsmd.getColumnCount()-1)
                sql += ", ";
        }
        sql += ") VALUES (";
        for(int i=0; i<rsmd.getColumnCount(); i++)
        {
            sql += "?";
            if(i<rsmd.getColumnCount()-1)
                sql += ", ";
            else
                sql+="); ";
        }
        jdbcTemplate2.update(sql,pola);
    }

    public static ResultSetMetaData podajMetaDaneTabeli(String tabela) throws SQLException {
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/SKM?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC","user","root");

        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM "+tabela);
        ResultSetMetaData rsmd = rs.getMetaData();

        return rsmd;
    }



    public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(Aplikacja.class);

        builder.headless(false);
        ConfigurableApplicationContext context = builder.run(args);
    }

    @Override
    public void run(String... strings) throws Exception {

       // jdbcTemplate.execute("INSERT INTO ETATY(Nazwa_etatu, Placa_min, Placa_max, Wymagana_liczba_godzin) VALUES ('inny', 500.50, 723.32,40);");
        List<Etat> etaty = jdbcTemplate.query("Select * from etaty;", new Object[]{}, new EtatRowMapper());
        for (Etat e: etaty)
            System.out.println(e.getNazwa() + " " + e.getMinPlaca() + " " + e.getMaxPlaca() + " " + e.getLiczbaGodz());
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/SKM?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC","user","root");

        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM pracownicy");
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnsNumber = rsmd.getColumnCount();

        /*System.out.println(columnsNumber);
        for (int x = 0; x < columnsNumber; x++ ){
            System.out.println(rsmd.getColumnName(x + 1));
        }
        */
        GUI g = new GUI(this);
        g.Menu();

    }
}
