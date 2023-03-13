package co.bharat.maxsociety.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class FlatsId implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5486729824810103276L;

	private String flatNo;
	
	private String wing;
	
	private String tower;
}
