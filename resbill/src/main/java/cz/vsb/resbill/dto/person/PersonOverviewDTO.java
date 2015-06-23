package cz.vsb.resbill.dto.person;

import cz.vsb.resbill.model.Person;

public class PersonOverviewDTO extends PersonDTO {

	private static final long serialVersionUID = -6358501872046330767L;

	private String note;

	public PersonOverviewDTO(Person p) {
		super(p);
	}

	@Override
	protected void fill(Person p) {
		super.fill(p);
		if (p != null) {
			this.note = p.getNote();
		}
	}

	public String getNote() {
		return note;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PersonOverviewDTO [");
		builder.append(super.toString());
		builder.append(", note=");
		builder.append(note);
		builder.append("]");
		return builder.toString();
	}

}
