package co.bharat.maxsociety.entity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "GateKeepRequest")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GateKeepRequest {

	@Id
	/*
	 * @GeneratedValue(generator = "uuid2")
	 * 
	 * @GenericGenerator(name = "uuid2", strategy =
	 * "org.hibernate.id.UUIDGenerator")
	 * 
	 * @Column(columnDefinition = "uuid", updatable = false, nullable = false)
	 */
	private String id;

	@NotNull
	private String guardId;

	@NotNull
	private String flatNo;

	@NotNull
	private String visitorName;

	private String visitPurpose;

	@NotNull
	private Date gkReqInitTime;
	
    @PrePersist
    protected void onCreate() {
    	gkReqInitTime = new Date();
    }

	@NotNull
	private String status;

	private Date gkReqActionTime;

	@NotNull
	private String title;

	@NotNull
	private String body;

}
