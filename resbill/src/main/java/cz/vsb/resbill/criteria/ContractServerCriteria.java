package cz.vsb.resbill.criteria;

import java.io.Serializable;
import java.util.Date;

import cz.vsb.resbill.model.ContractServer;

/**
 * A criteria class for searching {@link ContractServer} entities.
 * 
 * @author HAL191
 *
 */
public class ContractServerCriteria implements Serializable {

	private static final long serialVersionUID = 3773838281735011642L;

	public static enum OrderBy {
		PERIOD
	}

	private Integer contractId;

	private Integer serverId;

	private Boolean currentlyAssociated;

	private Date associatedFrom;

	private Date associatedTo;

	private boolean fetchContract;

	private boolean fetchServer;

	private OrderBy orderBy;

	public Integer getContractId() {
		return contractId;
	}

	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}

	public Integer getServerId() {
		return serverId;
	}

	public void setServerId(Integer serverId) {
		this.serverId = serverId;
	}

	public Boolean getCurrentlyAssociated() {
		return currentlyAssociated;
	}

	public void setCurrentlyAssociated(Boolean currentlyAssociated) {
		this.currentlyAssociated = currentlyAssociated;
	}

	public Date getAssociatedFrom() {
		return associatedFrom;
	}

	public void setAssociatedFrom(Date from) {
		this.associatedFrom = from;
	}

	public Date getAssociatedTo() {
		return associatedTo;
	}

	public void setAssociatedTo(Date to) {
		this.associatedTo = to;
	}

	public boolean isFetchContract() {
		return fetchContract;
	}

	public void setFetchContract(boolean fetchContract) {
		this.fetchContract = fetchContract;
	}

	public boolean isFetchServer() {
		return fetchServer;
	}

	public void setFetchServer(boolean fetchServer) {
		this.fetchServer = fetchServer;
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
		builder.append("ContractServerCriteria [");
		builder.append(super.toString());
		builder.append(", contractId=");
		builder.append(contractId);
		builder.append(", serverId=");
		builder.append(serverId);
		builder.append(", currentlyAssociated=");
		builder.append(currentlyAssociated);
		builder.append(", associatedFrom=");
		builder.append(associatedFrom);
		builder.append(", associatedTo=");
		builder.append(associatedTo);
		builder.append(", fetchContract=");
		builder.append(fetchContract);
		builder.append(", fetchServer=");
		builder.append(fetchServer);
		builder.append(", orderBy=");
		builder.append(orderBy);
		builder.append("]");
		return builder.toString();
	}

}
