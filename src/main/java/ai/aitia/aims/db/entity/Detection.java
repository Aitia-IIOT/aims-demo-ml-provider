package ai.aitia.aims.db.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Detection {
	
	//=================================================================================================
	// members
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(nullable = false)
	private long timestamp;
	
	@Column(nullable = false, length = 255)
	private String location;
	
	//=================================================================================================
	// methods

	//-------------------------------------------------------------------------------------------------
	public Detection() {}
	
	//-------------------------------------------------------------------------------------------------
	public Detection(final long timestamp, final String location) {
		this.timestamp = timestamp;
		this.location = location;
	}

	//-------------------------------------------------------------------------------------------------
	public long getId() { return id; }
	public long getTimestamp() { return timestamp; }
	public String getLocation() { return location; }

	//-------------------------------------------------------------------------------------------------
	public void setId(final long id) { this.id = id; }
	public void setTimestamp(final long timestamp) { this.timestamp = timestamp; }
	public void setLocation(final String location) { this.location = location; }

	//-------------------------------------------------------------------------------------------------
	@Override
	public String toString() {
		return "Detection [id=" + id + ", timestamp=" + timestamp
				+ ", location=" + location + "]";
	}
}
