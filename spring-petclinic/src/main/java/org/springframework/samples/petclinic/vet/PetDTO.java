package org.springframework.samples.petclinic.vet;

import java.util.List;
import org.springframework.samples.petclinic.visit.Visit;

public class PetDTO {

	private String name;

	private String owner;

	private List<Visit> visits;

	public PetDTO() {
	}

	public PetDTO(String name, String owner, List<Visit> visits) {
		this.name = name;
		this.owner = owner;
		this.visits = visits;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public List<Visit> getVisits() {
		return visits;
	}

	public void setVisits(List<Visit> visits) {
		this.visits = visits;
	}

}
