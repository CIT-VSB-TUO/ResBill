package cz.vsb.resbill.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "PRODUCTION_LEVEL")
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

	@Column(name = "code", length = 10, nullable = false, unique = true)
	private String code;

	@Column(name = "title", length = 250, nullable = false)
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
