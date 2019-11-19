package com.lion.skiresortbackend.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Repository;

import com.lion.skiresortbackend.entity.LiftRideRead;

@Repository
public class LiftRideReadRepositoryImpl implements LiftRideReadRepository {
	
	@Autowired
    private JdbcTemplate jdbcTemplate;

	@Override
	public boolean save(LiftRideRead liftRideRead) {
		String query="insert into liftrideread (id, liftId, timeInMin, vertical) values(?,?,?,?) on duplicate key update vertical=vertical+?";  
	    return jdbcTemplate.execute(query,new PreparedStatementCallback<Boolean>(){  
		    @Override  
		    public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {  
		    	ps.setString(1, liftRideRead.getId());
                ps.setInt(2, liftRideRead.getLiftId());
                ps.setInt(3,liftRideRead.getTime());
                ps.setInt(4,liftRideRead.getVertical());
                ps.setInt(5,liftRideRead.getVertical());
                return ps.execute(); 
		              
		    }  
	    });  
	}


	@Override
    public int[] batchInsert(List<LiftRideRead> liftRideReads) {
		int[] updateCounts = jdbcTemplate.batchUpdate(
	        "insert into liftrideread (id, liftId, timeInMin, vertical) values(?,?,?,?) on duplicate key update vertical=vertical+?",
	        new BatchPreparedStatementSetter()  {
	        	
	        	@Override
	            public void setValues(PreparedStatement ps, int i) throws SQLException {
	            	LiftRideRead argument = liftRideReads.get(i);
	                ps.setString(1, argument.getId());
	                ps.setInt(2, argument.getLiftId());
	                ps.setInt(3,argument.getTime());
	                ps.setInt(4,argument.getVertical());
	                ps.setInt(5,argument.getVertical());
	            }
	
				@Override
				public int getBatchSize() {
					return liftRideReads.size();
				}
	            
	        });

        return updateCounts;
	}


	@Override
	public List<LiftRideRead> findAll() {
		String sql = "select * from liftrideread";
		return jdbcTemplate.query(
                sql,
                (rs, rowNum) ->
                new LiftRideRead(rs.getString("id"),
        				rs.getInt("liftId"),
        				rs.getInt("timeInMin"),
        				rs.getInt("vertical"))
        );
	}

	@Override
	public LiftRideRead findById(String id) {
		String sql = "select * from liftrideread where id = ?";
		List<LiftRideRead> liftRideReads = jdbcTemplate.query(sql,
				new Object[]{id},
                (rs, rowNum) -> new LiftRideRead(rs.getString("id"),
                        				rs.getInt("liftId"),
                        				rs.getInt("timeInMin"),
                        				rs.getInt("vertical")));

		if (liftRideReads.size() == 1) {
			return liftRideReads.get(0);
		} else {
			return null;
		}
		
                
	}

}
