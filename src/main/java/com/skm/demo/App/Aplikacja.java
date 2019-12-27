package com.skm.demo.App;

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

@SpringBootApplication
public class Aplikacja implements CommandLineRunner {

    @Autowired
    JdbcTemplate jdbcTemplate;


    public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(Aplikacja.class);

        builder.headless(false);
        ConfigurableApplicationContext context = builder.run(args);
    }


    @Override
    public void run(String... strings) throws Exception {

        //jdbcTemplate.execute("INSERT INTO ETATY(Nazwa_etatu, Placa_min, Placa_max, Wymagana_liczba_godzin) VALUES ('testowy', 200.50, 423.32,40);");
        Etat etat = jdbcTemplate.queryForObject("Select * from etaty;", new Object[]{}, new EtatRowMapper());
        System.out.println(etat.getNazwa() + " " + etat.getMinPlaca() + " " + etat.getMaxPlaca() + " " + etat.getLiczbaGodz());
        Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/SKM?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC","user","root");
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM pracownicy");
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnsNumber = rsmd.getColumnCount();

        System.out.println(columnsNumber);
        for (int x = 0; x < columnsNumber; x++ ){
            System.out.println(rsmd.getColumnName(x + 1));
        }
        GUI g = new GUI();
        g.Menu();

    }
}
