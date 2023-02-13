package co.bharat.maxsociety.entity;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Embeddable
@Data
//@NoArgsConstructor
@AllArgsConstructor
public class FlatCompositeId {
	
	private String flatNo;
	
	private Long societyCode;
}
