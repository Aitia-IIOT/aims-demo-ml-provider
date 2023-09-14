package ai.aitia.aims.dto;

import java.io.Serializable;
import java.util.Objects;

public class DetectionDTO implements Serializable {
	
	//=================================================================================================
	// members
	
	private static final long serialVersionUID = 8126687377246826180L;
	
	private long timestamp;
	private String location;

	//=================================================================================================
	// methods 
	
	//-------------------------------------------------------------------------------------------------
	public DetectionDTO() {}
	
	//-------------------------------------------------------------------------------------------------
	public DetectionDTO(final long timestamp, final String location) {
		this.timestamp = timestamp;
		this.location = location;
	}

	//-------------------------------------------------------------------------------------------------
	public long getTimestamp() { return timestamp; }
	public String getLocation() { return location; }

	//-------------------------------------------------------------------------------------------------
	public void setTimestamp(final long timestamp) { this.timestamp = timestamp; }
	public void setLocation(final String location) { this.location = location; }

	//-------------------------------------------------------------------------------------------------
	@Override
	public int hashCode() {
		return Objects.hash(location, timestamp);
	}

	//-------------------------------------------------------------------------------------------------
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (obj == null) {
			return false;
		
		}
		
		if (getClass() != obj.getClass()) {
			return false;
		}
		
		final DetectionDTO other = (DetectionDTO) obj;
		return Objects.equals(location, other.location)
				&& timestamp == other.timestamp;
	}

	//-------------------------------------------------------------------------------------------------
	@Override
	public String toString() {
		return "DetectionDTO [timestamp=" + timestamp + ", location=" + location
				+ "]";
	}
}
