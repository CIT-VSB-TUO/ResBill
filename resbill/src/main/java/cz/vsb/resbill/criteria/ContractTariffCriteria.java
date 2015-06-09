package cz.vsb.resbill.criteria;

import java.io.Serializable;
import java.util.List;

import cz.vsb.resbill.model.ContractTariff;

/**
 * A criteria class for searching {@link ContractTariff} entities.
 * 
 * @author HAL191
 *
 */
public class ContractTariffCriteria implements Serializable {

	private static final long serialVersionUID = 3752663052905075572L;

	public static enum OrderBy {
		PERIOD_ASC, PERIOD_DESC
	}

	private Integer contractId;

	private Integer tariffId;

	private List<OrderBy> orderBy;

	private boolean fetchContract;

	private boolean fetchTariff;

	public Integer getContractId() {
		return contractId;
	}

	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}

	public Integer getTariffId() {
		return tariffId;
	}

	public void setTariffId(Integer tariffId) {
		this.tariffId = tariffId;
	}

	public List<OrderBy> getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(List<OrderBy> orderBy) {
		this.orderBy = orderBy;
	}

	public boolean isFetchContract() {
		return fetchContract;
	}

	public void setFetchContract(boolean fetchContract) {
		this.fetchContract = fetchContract;
	}

	public boolean isFetchTariff() {
		return fetchTariff;
	}

	public void setFetchTariff(boolean fetchTariff) {
		this.fetchTariff = fetchTariff;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ContractTariffCriteria [");
		builder.append(super.toString());
		builder.append(", contractId=");
		builder.append(contractId);
		builder.append(", tariffId=");
		builder.append(tariffId);
		builder.append(", orderBy=");
		builder.append(orderBy);
		builder.append(", fetchContract=");
		builder.append(fetchContract);
		builder.append(", fetchTariff=");
		builder.append(fetchTariff);
		builder.append("]");
		return builder.toString();
	}

}
