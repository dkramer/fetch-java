package dev.dkramer.fetch.challenge.repository;

import dev.dkramer.fetch.challenge.repository.models.Receipt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Optional;

@Repository
public class ReceiptRepository {

    Logger log = LoggerFactory.getLogger(ReceiptRepository.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void insertNewReceipt(Receipt receipt) {
        try {
            try (
                    Connection con = jdbcTemplate.getDataSource().getConnection();
                    PreparedStatement ps = con.prepareStatement(
                            "INSERT INTO receipt " +
                                    "( " +
                                    "receipt_id, " +
                                    "retailer, " +
                                    "purchase_date, " +
                                    "purchase_time, " +
                                    "total, " +
                                    "points " +
                                    ") " +
                                    "VALUES " +
                                    "( " +
                                    "?, " +
                                    "?, " +
                                    "?, " +
                                    "?, " +
                                    "?, " +
                                    "? " +
                                    ") "
                    )
            ) {
                ps.setString(1, receipt.getReceiptId());
                ps.setString(2, receipt.getRetailer());
                ps.setDate(3, Date.valueOf(receipt.getPurchaseDate()));
                ps.setTime(4, Time.valueOf(receipt.getPurchaseTime()));
                ps.setDouble(5, receipt.getTotal());
                ps.setLong(6, receipt.getPoints());
                ps.execute();
                con.commit();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public Optional<Receipt> getReceiptById(String id) {
        Optional<Receipt> optionalReceipt = Optional.empty();
        try {
            try (
                    Connection con = jdbcTemplate.getDataSource().getConnection();
                    PreparedStatement ps = con.prepareStatement(
                            "SELECT " +
                                    "retailer, " +
                                    "purchase_date, " +
                                    "purchase_time, " +
                                    "total, " +
                                    "points " +
                                    "FROM receipt " +
                                    "WHERE receipt_id = ? "
                    )
            ) {
                ps.setString(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    while(rs.next()) {
                        optionalReceipt = Optional.of(Receipt
                                .builder()
                                        .receiptId(id)
                                        .retailer(rs.getString(1))
                                        .purchaseDate(rs.getDate(2).toLocalDate())
                                        .purchaseTime(rs.getTime(3).toLocalTime())
                                        .total(rs.getDouble(4))
                                        .points(rs.getLong(5))
                                        .receiptItems(Collections.emptyList())
                                .build());
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return optionalReceipt;
    }
}
