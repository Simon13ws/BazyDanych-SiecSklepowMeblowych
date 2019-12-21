package com.skm.demo.RowMappers;

import com.skm.demo.Entities.Sklep;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SklepRowMapper implements RowMapper<Sklep> {

    @Override
    public Sklep mapRow(ResultSet rs, int rowNum) throws SQLException {

        Sklep sklep = new Sklep(rs.getString("adres"),
                rs.getString("miasto"),
                rs.getInt("kod_pocztowy"),
                rs.getInt("id_kierownika"),
                rs.getInt("id_sklepu")
        );
        return sklep;

    }
}