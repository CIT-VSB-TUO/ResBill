package cz.vsb.resbill.criteria;

import java.io.Serializable;

public class ServerCriteria implements Serializable {

	private static final long serialVersionUID = -1967413195158720777L;

	public static enum OrderBy {
		SERVER_ID, NAME
	}

	private String serverId;

	private String namePrefix;

	private Boolean used;

	private Boolean inContract;

	private OrderBy orderBy;

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public String getNamePrefix() {
		return namePrefix;
	}

	public void setNamePrefix(String namePrefix) {
		this.namePrefix = namePrefix;
	}

	public Boolean getUsed() {
		return used;
	}

	public void setUsed(Boolean used) {
		this.used = used;
	}

	public Boolean getInContract() {
		return inContract;
	}

	public void setInContract(Boolean inContract) {
		this.inContract = inContract;
	}

	public OrderBy getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(OrderBy orderBy) {
		this.orderBy = orderBy;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ServerCriteria [");
		builder.append(super.toString());
		builder.append(", serverId=");
		builder.append(serverId);
		builder.append(", namePrefix=");
		builder.append(namePrefix);
		builder.append(", used=");
		builder.append(used);
		builder.append(", inContract=");
		builder.append(inContract);
		builder.append(", orderBy=");
		builder.append(orderBy);
		builder.append("]");
		return builder.toString();
	}

}
