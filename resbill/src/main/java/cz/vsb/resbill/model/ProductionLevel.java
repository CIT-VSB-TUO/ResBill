package cz.vsb.resbill.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "PRODUCTION_LEVEL", uniqueConstraints = @UniqueConstraint(name = "UK_production_level__code", columnNames = "code"))
public class ProductionLevel extends BaseEnumeratedIdEntity {

	private static final long serialVersionUID = 42165378614212986L;

	public static final Integer P1 = 1;

	public static final Integer P2 = 2;

	public static final Integer P3 = 3;

	public static final Integer TESTING = 4;

	public static final Integer OFF1 = 5;

	public static final Integer OFF2 = 6;

	public static final Integer OFF3 = 7;

	public static final Integer OFF_TESTING = 8;

	@Column(name = "code")
	@NotEmpty
	@Size(max = 20)
	private String code;

	@Column(name = "title")
	@Size(max = 250)
	private String title;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ProductionLevel [");
		builder.append(super.toString());
		builder.append(", code=");
		builder.append(code);
		builder.append(", title=");
		builder.append(title);
		builder.append("]");
		return builder.toString();
	}

}
