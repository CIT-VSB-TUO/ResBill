package cz.vsb.resbill.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "FILE")
public class File extends BaseGeneratedIdEntity {

	private static final long serialVersionUID = 857967863538385522L;

	@Column(name = "name")
	@NotEmpty
	@Size(max = 250)
	private String name;

	@Column(name = "size")
	@NotNull
	private Long size;

	@Column(name = "content_type")
	@Size(max = 250)
	private String contentType;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "content")
	private byte[] content;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("File [");
		builder.append(super.toString());
		builder.append(", name=");
		builder.append(name);
		builder.append(", size=");
		builder.append(size);
		builder.append(", contentType=");
		builder.append(contentType);
		builder.append("]");
		return builder.toString();
	}

}
