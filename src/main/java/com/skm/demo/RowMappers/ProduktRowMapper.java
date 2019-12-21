package com.skm.demo.RowMappers;

import com.skm.demo.Entities.Produkt;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProduktRowMapper implements RowMapper<Produkt> {

    @Override
    public Produkt mapRow(ResultSet rs, int rowNum) throws SQLException {

        Produkt produkt = new Produkt(rs.getString("nazwa"),
                rs.getInt("numer_serii"),
                rs.getFloat("cena_bazowa"),
                rs.getInt("liczba"),
                rs.getInt("dzialy_id_dzialu"),
                rs.getInt("dostawcy_id_dostawcy"),
                rs.getFloat("cena_aktualna")
        );
        return produkt;

    }
}