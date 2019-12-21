package com.skm.demo.RowMappers;

import com.skm.demo.Entities.Dostawca;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DostawcaRowMapper implements RowMapper<Dostawca> {

    @Override
    public Dostawca mapRow(ResultSet rs, int rowNum) throws SQLException {

        Dostawca dostawca = new Dostawca(rs.getString("nazwa"),
                rs.getString("email"),
                rs.getInt("id_dostawcy")
        );
        return dostawca;

    }
}

