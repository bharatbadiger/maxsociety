package co.bharat.maxsociety.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class FlatsId implements Serializable{

	private String flatNo;
	
	private String wing;
	
	private String tower;
}
