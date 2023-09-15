package ai.aitia.aims.db.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ai.aitia.aims.db.entity.Detection;

@Repository
public interface DetectionRepository extends JpaRepository<Detection,Long> {

	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	public List<Detection> findByTimestampGreaterThan(final long timestamp);
}
