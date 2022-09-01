/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.vet;

import java.util.List;

import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.owner.PetRepository;
import org.springframework.samples.petclinic.visit.Visit;
import org.springframework.samples.petclinic.visit.VisitRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * @author Juergen Hoeller
 * @author Mark Fisher
 * @author Ken Krebs
 * @author Arjen Poutsma
 */
@Controller
class VetController {

	@Autowired
	private VetRepository vets;

	@Autowired
	private PetRepository petRepository;

	@Autowired
	private VisitRepository visitRepository;

	@GetMapping("/vets.html")
	public String showVetList(@RequestParam(defaultValue = "1") int page, Model model) {
		// Here we are returning an object of type 'Vets' rather than a collection of Vet
		// objects so it is simpler for Object-Xml mapping
		Vets vets = new Vets();
		Page<Vet> paginated = findPaginated(page);
		vets.getVetList().addAll(paginated.toList());
		return addPaginationModel(page, paginated, model);

	}

	private String addPaginationModel(int page, Page<Vet> paginated, Model model) {
		List<Vet> listVets = paginated.getContent();
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", paginated.getTotalPages());
		model.addAttribute("totalItems", paginated.getTotalElements());
		model.addAttribute("listVets", listVets);
		return "vets/vetList";
	}

	private Page<Vet> findPaginated(int page) {
		Pageable pageable = PageRequest.of(page - 1, 5);
		return vets.findAll(pageable);
	}

	@GetMapping({ "/vets" })
	public @ResponseBody Vets showResourcesVetList() {
		// Here we are returning an object of type 'Vets' rather than a collection of Vet
		// objects so it is simpler for JSon/Object mapping
		Vets vets = new Vets();
		vets.getVetList().addAll(this.vets.findAll());
		return vets;
	}

	@GetMapping({ "/vetDetails" })
	public @ResponseBody List<VetDTO> showDetailedResourcesVetList(int page) {
		return vets.findAllByOrderById(Pageable.ofSize(5).withPage(page)).stream().map(vet -> {
			VetDTO newVet = new VetDTO();
			newVet.setId(vet.getId());
			newVet.setLastName(vet.getLastName());
			newVet.setFirstName(vet.getFirstName());
			Set<PetDTO> pets = findPetDTOSet(vet.getId());
			newVet.setPets(pets);
			return newVet;
		}).collect(Collectors.toList());
	}

	@Cacheable(key = "petOwner", cacheNames = "petCache")
	public Set<PetDTO> findPetDTOSet(Integer vetId) {
		List<Visit> visits = visitRepository.findByVetId(vetId);
		return visits.stream().distinct().map(visit -> {
			Pet current = petRepository.findById(visit.getPetId());
			return new PetDTO(current.getName(), current.getOwner().getLastName(),
					visitRepository.findByPetId(current.getId()));
		}).collect(Collectors.toSet());
	}

}
