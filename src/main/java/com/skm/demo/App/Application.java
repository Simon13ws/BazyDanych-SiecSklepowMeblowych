package com.skm.demo.App;


import com.skm.demo.Entities.Etat;
import com.skm.demo.RowMappers.EtatRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    JdbcTemplate jdbcTemplate;


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


    @Override
    public void run(String... strings) throws Exception {

        //jdbcTemplate.execute("INSERT INTO ETATY(Nazwa_etatu, Placa_min, Placa_max, Wymagana_liczba_godzin) VALUES ('testowy', 200.50, 423.32,40);");
        Etat etat = jdbcTemplate.queryForObject("Select * from etaty;", new Object[]{}, new EtatRowMapper());
        System.out.println(etat.getNazwa() + " " + etat.getMinPlaca() + " " + etat.getMaxPlaca() + " " + etat.getLiczbaGodz());

    }
}
