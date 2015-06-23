package cz.vsb.resbill.dto.person;

import cz.vsb.resbill.model.Person;

public class PersonHeaderDTO extends PersonDTO {

	private static final long serialVersionUID = 6438283064076875660L;

	public PersonHeaderDTO(Person p) {
		super(p);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PersonHeaderDTO [");
		builder.append(super.toString());
		builder.append(", ]");
		return builder.toString();
	}

}
