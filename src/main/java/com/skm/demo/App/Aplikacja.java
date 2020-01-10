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

    public static LinkedHashMap<Integer, String> getPKID(String tabela, Aplikacja a) throws SQLException {

        DatabaseMetaData md = con.getMetaData();
        LinkedHashMap<Integer, String> pk = new LinkedHashMap<Integer, String>();
        ResultSet rsPk = md.getPrimaryKeys(null,null,tabela);
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM "+tabela);
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnsCount = rsmd.getColumnCount();
        // rsPK nie ma nazwy kolumn z klucza głównego
        if(rsPk.next()) {
            for (int i = 1; i <= columnsCount; i++) {
                String pkName = rsPk.getString("COLUMN_NAME");
                if (rsmd.getColumnName(i).equals(pkName))
                    pk.put(i, pkName);
            }
        }
        return pk;
    }

    public static String[][] selectAll(String tabela) throws SQLException {
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM " + tabela);

        int rowsCount = 0;
        if(rs.last())
        {
            rowsCount = rs.getRow();
            rs.beforeFirst();
        }
        int columnsCount = rs.getMetaData().getColumnCount();
        String[][] entities = new String[rowsCount][columnsCount];
        int i = 1;
        while(rs.next())
        {
            for(int col=1; col<= columnsCount; col++)
            {
                Object value = rs.getObject(col);
                if(value != null)
                    entities[i-1][col-1] = value.toString();
                else
                    entities[i-1][col-1] = "";
            }
            i++;
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
    public static void dodajWiersz(String [] pola, String tabela, Aplikacja a) throws SQLException {
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
        System.out.println("x2");
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
