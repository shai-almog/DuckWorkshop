package org.springframework.samples.petclinic.vet;

import java.util.List;
import java.util.Set;
import org.springframework.samples.petclinic.model.Person;

/**
 * DTO containing the data about the vet and some additional meta data
 */

public class VetDTO extends Person {

	private Set<PetDTO> pets;

	public Set<PetDTO> getPets() {
		return pets;
	}

	public void setPets(Set<PetDTO> pets) {
		this.pets = pets;
	}

}
