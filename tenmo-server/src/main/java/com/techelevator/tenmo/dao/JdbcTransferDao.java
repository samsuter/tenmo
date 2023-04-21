package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Transfer> getUserTransfers(int userId) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT * FROM transfer WHERE account_from = (\n" +
                "SELECT account_id FROM account WHERE user_id = ?\n" +
                ") OR account_to = (\n" +
                "SELECT account_id FROM account WHERE user_id = ?\n" +
                ")";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, userId);

        while (results.next()) {
            Transfer transfer = mapToTransfer(results);
            transfers.add(transfer);
        }
        return transfers;
    }

    @Override
    public Transfer getTransfer(int transferId) {
        Transfer transfer = new Transfer();

        String sql = "SELECT * FROM transfer WHERE transfer_id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, transferId);

        if(result.next()) {
            transfer = mapToTransfer(result);
            return transfer;
        } else {
            return null;

        }
    }



    @Override
    public Transfer createTransfer(Transfer newTransfer) {
        String sql = "INSERT INTO transfer VALUES (DEFAULT, ?, ?, \n" +
                "(\n" +
                "SELECT account_id FROM account WHERE user_id = ?\n" +
                "),  \n" +
                "(\n" +
                "SELECT account_id FROM account WHERE user_id = ?\n" +
                "), ?) RETURNING transfer_id";
        Integer newId = jdbcTemplate.queryForObject(sql, Integer.class, newTransfer.getTransferTypeId(), newTransfer.getTransferStatusId(),
                newTransfer.getAccountTo(), newTransfer.getAccountFrom(), newTransfer.getAmount());
        newTransfer.setTransferId(newId);
        return newTransfer;
    }

    private Transfer mapToTransfer(SqlRowSet results) {
       Transfer newTransfer = new Transfer();

       newTransfer.setAccountFrom(results.getInt("account_from"));
       newTransfer.setAccountTo(results.getInt("account_to"));
       newTransfer.setAmount(results.getBigDecimal("amount"));
       newTransfer.setTransferId(results.getInt("transfer_id"));
       newTransfer.setTransferStatusId(results.getInt("transfer_status_id"));
       newTransfer.setTransferTypeId(results.getInt("transfer_type_id"));

        return newTransfer;
    }

}
