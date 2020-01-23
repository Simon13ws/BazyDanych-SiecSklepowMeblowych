package com.skm.demo.App;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;

import java.sql.*;
import java.util.*;

@SpringBootApplication
public class Aplikacja implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private static Connection con;
    private String conStr = "jdbc:mysql://localhost:3306/SKM?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private String user = "user";
    private String pass = "root";

    public static LinkedHashMap<Integer, String> getKeys(String tabela, Aplikacja a, String keyType, int pkOrTable) throws SQLException {

        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/SKM?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC","user","root");
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
                    if (keyType.equals("primary")) {
                        kName = rsKeys.getString("COLUMN_NAME");
                    } else {
                        kName = rsKeys.getString("FKCOLUMN_NAME");
                    }
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
        con.close();
        return keys;
    }

    public static String checkType(String tabela, String kolumna, String wartosc, Aplikacja a) throws SQLException {
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/SKM?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC","user","root");
        JdbcTemplate jdbcTemplate2 = a.getJDBC();

        SimpleJdbcCall simpleJdbc = new SimpleJdbcCall(jdbcTemplate2).withFunctionName("checkType");
        LinkedHashMap<String, Object> in = new LinkedHashMap<>();
        in.put("tabela", tabela);
        in.put("kolumna", kolumna);
        in.put("wartosc", wartosc);
        String tresc = simpleJdbc.executeFunction(String.class, in);
        con.close();
        return tresc;
    }

    public static String podajMarze(String id_sklepu) throws SQLException {
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/SKM?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC","user","root");
        String sql = "SELECT SUM(p.cena_aktualna)*0.1*p.liczba as Marża " +
                        "from dzialy d " +
                        "INNER JOIN sklepy s on s.id_sklepu=d.sklepy_id_sklepu " +
                        "INNER JOIN produkty p on p.dzialy_id_dzialu=d.id_dzialu " +
                        "where id_sklepu = " + id_sklepu;

        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(sql);
        rs.next();
        String marza = rs.getString("Marża");
        con.close();
        return marza;
    }

    public static String podajLiczbePracownikow(String id_sklepu) throws SQLException {
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/SKM?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC","user","root");
        String sql = "SELECT COUNT(*) as LiczbaPracownikow " +
                        "from pracownicy p " +
                        "INNER JOIN sklepy s on s.id_sklepu=p.sklepy_id_sklepu " +
                        "where id_sklepu = " + id_sklepu;

        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(sql);
        rs.next();
        String liczbaPracownikow = rs.getString("LiczbaPracownikow");
        con.close();
        return liczbaPracownikow;
    }

    public static int [] getNextIncs(String[] tabele, Aplikacja a) throws SQLException {
        int [] incs = new int [tabele.length];

        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/SKM?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC","user","root");
        JdbcTemplate jdbcTemplate2 = a.getJDBC();

        for(int i=0; i<tabele.length; i++){
            jdbcTemplate2.execute("Analyze Table " + tabele[i]);
            incs[i] = getNextNumber(tabele[i]);
        }

        con.close();
        return incs;
    }

    public static int getNextNumber(String tabela) throws SQLException{
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/SKM?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC","user","root");
        String sql = "SELECT AUTO_INCREMENT FROM INFORMATION_SCHEMA.TABLES \n" +
                "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = '" + tabela + "'";
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(sql);

        rs.next();
        int x = rs.getInt("AUTO_INCREMENT");
        con.close();
        return x;
    }

    public static ArrayList<String> selectColumn(String tabela, String column) throws SQLException {
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/SKM?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC","user","root");
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("SELECT " + column + " FROM " + tabela);
        ArrayList<String> values = new ArrayList<String>();
        int i = 0;
        while(rs.next())
        {
            values.add(rs.getObject(1).toString());
            i++;
        }
        con.close();
        return values;
    }

    public static ArrayList<String> getColumnNames(String tabela) throws SQLException {

        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/SKM?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC","user","root");
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM "+tabela + " LIMIT 1");
        ResultSetMetaData rsmd = rs.getMetaData();
        ArrayList<String> columnNames = new ArrayList<String>();
        int columnsCount = rsmd.getColumnCount();
        for(int i=1; i<=columnsCount; i++)
            columnNames.add(rsmd.getColumnName(i));
        con.close();
        return columnNames;
    }

    public static LinkedHashSet<ArrayList<String>> szukaj(String tabela, String kolumna, String wyrazenie) throws SQLException {
        String sql = "SELECT * FROM " + tabela + " Where " + kolumna + " regexp '^"+ wyrazenie + "';";
        return selectRows(sql);
    }

    public static LinkedHashSet<ArrayList<String>> selectAll(String tabela) throws SQLException {
        String sql = "SELECT * FROM " + tabela;
        return selectRows(sql);
    }

    public static LinkedHashSet<ArrayList<String>> selectRows(String sql) throws SQLException {
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/SKM?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC","user","root");
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(sql);
        int columnsCount = rs.getMetaData().getColumnCount();
        LinkedHashSet<ArrayList<String>> entities = new LinkedHashSet<ArrayList<String>>();
        while(rs.next())
        {
            ArrayList<String> values = new ArrayList<String>();
            for(int col=1; col<= columnsCount; col++)
            {
                Object value = rs.getObject(col);
                if(value != null)
                    values.add(value.toString());
                else
                    values.add("");
            }
            entities.add(values);
        }
        con.close();
        return entities;
    }

    public static void deleteRow(String tabela, LinkedHashMap<String, Object> wartosci, Aplikacja a) throws SQLException {
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/SKM?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC","user","root");
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
            n++;
        }
        JdbcTemplate jdbcTemplate2 = a.getJDBC();
        jdbcTemplate2.update(sql);
        con.close();
    }

    public JdbcTemplate getJDBC(){
        return jdbcTemplate;
    }

    public static void updateRow(ArrayList<String> kolumny, ArrayList<String> wartosci, String tabela, LinkedHashMap<String, String> pk, Aplikacja a) throws SQLException{
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/SKM?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC","user","root");
        String sql = "UPDATE " + tabela + " SET ";
        for(int i=0; i<kolumny.size(); i++)
        {
            if(kolumny.get(i).contains("id") || kolumny.get(i).contains("numer"))
                sql += kolumny.get(i) + " = " + wartosci.get(i);
            else
                sql += kolumny.get(i) + " = '" + wartosci.get(i) + "'";
            if(i<kolumny.size())
                sql += ", ";
            else
                sql += " WHERE ";
        }
        int n = 0;
        for(Map.Entry<String,String> w: pk.entrySet()) {
            if(w.getKey().contains("id") || w.getKey().contains("numer"))
                sql += w.getKey() + " = " + w.getValue();
            else
                sql += w.getKey() + " = '" + w.getValue() + "'";
            if(n < pk.size()-1)
                sql += " AND ";
            else
                sql += ";";
            n++;
        }
        JdbcTemplate jdbcTemplate2 = a.getJDBC();
        jdbcTemplate2.update(sql);
        con.close();
    }

    //Dodawanie dziala ale jeszcze nie ma sprawdzania kluczy głównych i poprawności danych
    public static void addRow(ArrayList<String> kolumny, ArrayList<String> wartosci, String tabela, Aplikacja a) throws SQLException {

        String sql = "INSERT INTO "+tabela+" (";
        for(int i=0; i<kolumny.size(); i++)
        {
            sql += kolumny.get(i);
            if(i<kolumny.size()-1)
                sql += ", ";
        }
        sql += ") VALUES (";
        for(int i=0; i<kolumny.size(); i++)
        {
            if(kolumny.get(i).contains("id") || kolumny.get(i).contains("numer"))
                sql += wartosci.get(i);
            else
                sql += "'" + wartosci.get(i)+"'";
            if(i<kolumny.size()-1)
                sql += ", ";
            else
                sql+="); ";
        }
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/SKM?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC","user","root");
        JdbcTemplate jdbcTemplate2 = a.getJDBC();
        jdbcTemplate2.update(sql);
        if(!tabela.equals("etaty") && !tabela.equals("promocja_produktu")) {
            int x = getNextNumber(tabela) + 1;
            jdbcTemplate2.update("Alter TABLE " + tabela + " AUTO_INCREMENT = " + String.valueOf(x));
        }
        con.close();
    }


    public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(Aplikacja.class);

        builder.headless(false);
        ConfigurableApplicationContext context = builder.run(args);
    }

    @Override
    public void run(String... strings) throws Exception {

        GUI g = new GUI(this);
        g.Menu();

    }
}
