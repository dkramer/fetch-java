package dev.dkramer.fetch.challenge.repository;

import dev.dkramer.fetch.challenge.repository.models.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;

@Repository
public class ItemRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;


    public void insertNewItems(List<Item> receiptItems) {
        try {
            try (
                    Connection con = jdbcTemplate.getDataSource().getConnection()) {

                receiptItems.forEach(item -> {
                    try (
                            PreparedStatement ps = con.prepareStatement(
                                    "INSERT INTO item " +
                                            "( " +
                                            "item_id, " +
                                            "short_description, " +
                                            "price, " +
                                            "receipt_id " +
                                            ") " +
                                            "VALUES " +
                                            "( " +
                                            "?, " +
                                            "?, " +
                                            "?, " +
                                            "? " +
                                            ") "
                            )
                    ) {
                        ps.setString(1, item.getItemId());
                        ps.setString(2, item.getShortDescription());
                        ps.setDouble(3, item.getPrice());
                        ps.setString(4, item.getReceiptId());
                        ps.execute();
                        con.commit();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
