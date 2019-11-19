package com.lion.skiresortbackend.repository;


import java.util.List;

import com.lion.skiresortbackend.entity.LiftRideRead;


public interface LiftRideReadRepository {
	
	boolean save(LiftRideRead liftRideRead);
	
	int[] batchInsert(List<LiftRideRead> liftRideReads);
	
    List<LiftRideRead> findAll();

    LiftRideRead findById(String id);
	

}
