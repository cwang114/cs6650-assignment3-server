package com.lion.skiresortbackend.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Repository;

import com.lion.skiresortbackend.entity.LiftRideWrite;

@Repository
public class LiftRideWriteRepositoryImpl implements LiftRideWriteRepository {
	@Autowired
	private JdbcTemplate jdbcTemplate;	
	
	@Override
	public boolean save(LiftRideWrite liftRideWrite) {
		String query="insert into liftridewrite (timeInMin, liftId, resortId, season, dayId, skierId, vertical) values(?,?,?,?,?,?,?)";  
	    return jdbcTemplate.execute(query,new PreparedStatementCallback<Boolean>(){  
		    @Override  
		    public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {  
		              
		    	ps.setInt(1, liftRideWrite.getTime());
				ps.setInt(2, liftRideWrite.getLiftId());
				ps.setInt(3, liftRideWrite.getResortId());
				ps.setInt(4, liftRideWrite.getSeason());
				ps.setInt(5, liftRideWrite.getDayId());
				ps.setInt(6, liftRideWrite.getSkierId());
				ps.setInt(7, liftRideWrite.getVertical());
				return ps.execute();  
		              
		    }  
	    });  
		
	}

	@Override
	public int[] batchInsert(List<LiftRideWrite> liftRideWrites) {
		int[] updateCounts = jdbcTemplate.batchUpdate(
				"insert into liftridewrite (timeInMin, liftId, resortId, season, dayId, skierId, vertical) values(?,?,?,?,?,?,?)",
				new BatchPreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						LiftRideWrite argument = liftRideWrites.get(i);
						ps.setInt(1, argument.getTime());
						ps.setInt(2, argument.getLiftId());
						ps.setInt(3, argument.getResortId());
						ps.setInt(4, argument.getSeason());
						ps.setInt(5, argument.getDayId());
						ps.setInt(6, argument.getSkierId());
						ps.setInt(7, argument.getVertical());

					}

					@Override
					public int getBatchSize() {
						return liftRideWrites.size();
					}

				});

		return updateCounts;
	}

	@Override
	public List<LiftRideWrite> findBySkierIdAndResortId(int skierId, int resortId) {
		String sql = "select * from liftridewrite where skierId = ? and resortId = ?";
		return queryWithThisSql(sql);
	}

	@Override
	public List<LiftRideWrite> findBySkierIdAndResortIdAndSeason(int skierId, int resortId, int season) {
		String sql = "select * from liftridewrite where skierId = ? and resortId = ? and season = ?";
		return queryWithThisSql(sql);

	}

	private List<LiftRideWrite> queryWithThisSql(String sql) {
		return jdbcTemplate.query(sql, (rs, rowNum) -> new LiftRideWrite(rs.getInt("resortId"), rs.getInt("season"),
				rs.getInt("dayId"), rs.getInt("skierId"), rs.getInt("liftId"), rs.getInt("timeInMin")));
	}
}
