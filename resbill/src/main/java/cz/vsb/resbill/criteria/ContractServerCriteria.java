package cz.vsb.resbill.criteria;

import java.io.Serializable;

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

	private Boolean currentlyValid;

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

	public Boolean getCurrentlyValid() {
		return currentlyValid;
	}

	public void setCurrentlyValid(Boolean currentlyValid) {
		this.currentlyValid = currentlyValid;
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
		builder.append(", currentlyValid=");
		builder.append(currentlyValid);
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
