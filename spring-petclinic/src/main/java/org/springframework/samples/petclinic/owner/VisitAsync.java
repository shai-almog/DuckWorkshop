package org.springframework.samples.petclinic.owner;

import org.springframework.samples.petclinic.visit.Visit;
import org.springframework.samples.petclinic.visit.VisitRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;

@Controller
public class VisitAsync {

	private final VisitRepository visits;

	public VisitAsync(VisitRepository visits) {
		this.visits = visits;
	}

	@Async
	public void saveVisit(Visit visit) {
		visits.save(visit);
	}

}
