package com.skm.demo;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EtatRowMapper implements RowMapper<Etat> {

        @Override
        public Etat mapRow(ResultSet rs, int rowNum) throws SQLException {

            Etat etat = new Etat();
            etat.nazwa = rs.getString("nazwa_etatu");
            etat.minPlaca = rs.getFloat("placa_min");
            etat.maxPlaca = rs.getFloat("placa_max");
            etat.liczbaGodz = rs.getInt("wymagana_liczba_godzin");

            return etat;

        }
}

