package io.fraud.database.dao;

import io.fraud.database.model.Deal;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import java.util.List;

@RegisterBeanMapper(Deal.class)
public interface DealDao {

    @SqlQuery("SELECT * FROM deals WHERE id = ?")
    Deal findById(int id);

    @SqlQuery("SELECT * FROM deals WHERE currency = ?")
    List <Deal> findByCurrency(String currency);

    @SqlQuery("SELECT * FROM deals WHERE source = ?")
    List <Deal> findBySource(String source);

}
