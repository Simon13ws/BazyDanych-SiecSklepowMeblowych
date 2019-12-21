package com.skm.demo.RowMappers;

import com.skm.demo.Entities.Promocja;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PromocjaRowMapper implements RowMapper<Promocja> {

    @Override
    public Promocja mapRow(ResultSet rs, int rowNum) throws SQLException {

        Promocja promocja = new Promocja(rs.getString("nazwa"),
                rs.getInt("id_promocji"),
                rs.getFloat("wysokosc_promocji")
        );
        return promocja;

    }
}

