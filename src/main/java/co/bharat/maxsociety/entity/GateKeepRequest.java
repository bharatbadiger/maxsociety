package co.bharat.maxsociety.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	/*
	 * @GeneratedValue(generator = "uuid2")
	 * 
	 * @GenericGenerator(name = "uuid2", strategy =
	 * "org.hibernate.id.UUIDGenerator")
	 * 
	 * @Column(columnDefinition = "uuid", updatable = false, nullable = false)
	 */
	private Long id;

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
		gkReqActionTime = gkReqInitTime;
	}

	@NotNull
	private String status;

	// private String notificationType;

	private Date gkReqActionTime;

	@PreUpdate
	protected void onUpdate() {
		gkReqActionTime = new Date();
	}

	@NotNull
	@Transient
	private String title;

	@NotNull
	@Transient
	private String body;

	private String path;

	/*
	 * public GateKeepRequest() { if(getGkReqInitTime() == null) { gkReqInitTime =
	 * new Date(); } if(getGkReqActionTime() == null) { gkReqActionTime = new
	 * Date(); } }
	 */

}
