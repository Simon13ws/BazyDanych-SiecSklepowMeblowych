package com.skm.demo.RowMappers;

import com.skm.demo.Entities.Etat;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EtatRowMapper implements RowMapper<Etat> {

        @Override
        public Etat mapRow(ResultSet rs, int rowNum) throws SQLException {

            Etat etat = new Etat(rs.getString("nazwa_etatu"),
                    rs.getFloat("placa_min"),
                    rs.getFloat("placa_max"),
                    rs.getInt("wymagana_liczba_godzin"));
            return etat;

        }
}

