package com.connectly_backend;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DatabaseController {
    private final DataSource dataSource;

    public DatabaseController(DataSource dataSource){
        this.dataSource = dataSource;
    }

    @GetMapping("/api/health/db")
    public String databaseHealth() throws SQLException {
        try (Connection connection = dataSource.getConnection())
        {
            if(connection.isValid(2))
            {
                return "Database Connected Successfully!";
            }
            return "Database connection failed";
        }
    }
}
