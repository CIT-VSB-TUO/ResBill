package cz.vsb.resbill.criteria;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cz.vsb.resbill.model.Contract;
import cz.vsb.resbill.util.ToStringBuilder;

/**
 * A criteria class for searching {@link Contract} entities.
 * 
 * @author HAL191
 *
 */
public class ContractCriteria implements Serializable, Cloneable {

	private static final long serialVersionUID = 3463991795938726531L;

	private List<OrderBy> orderBy = Arrays.asList(new OrderBy[] { OrderBy.EVIDENCE_NUMBER_ASC });

	private Set<Integer> contractIds = null;

	private Integer customerId = null;

	private EnumSet<Feature> features = null;

	/**
   * 
   */
	@Override
	public ContractCriteria clone() throws CloneNotSupportedException {
		ContractCriteria clone = (ContractCriteria) super.clone();

		clone.setOrderBy(orderBy != null ? new ArrayList<OrderBy>(orderBy) : null);
		clone.setContractIds(contractIds != null ? new HashSet<Integer>(contractIds) : null);
		clone.setFeatures(features != null ? features.clone() : null);

		return clone;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	/**
	 * @return the orderBy
	 */
	public List<OrderBy> getOrderBy() {
		return orderBy;
	}

	/**
	 * @param orderBy
	 *          the orderBy to set
	 */
	public void setOrderBy(List<OrderBy> orderBy) {
		this.orderBy = orderBy;
	}

	/**
	 * @return the features
	 */
	public EnumSet<Feature> getFeatures() {
		return features;
	}

	/**
	 * @param features
	 *          the features to set
	 */
	public void setFeatures(EnumSet<Feature> features) {
		this.features = features;
	}

	/**
	 * @return the contractIds
	 */
	public Set<Integer> getContractIds() {
		return contractIds;
	}

	/**
	 * @param contractIds
	 *          the contractIds to set
	 */
	public void setContractIds(Set<Integer> contractIds) {
		this.contractIds = contractIds;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("orderBy", orderBy);
		builder.append("customerId", customerId);
		builder.append("contractIds", contractIds);
		builder.append("features", features);
		builder.append("toString()", super.toString());
		return builder.toString();
	}

	/**
	 * Pri hledani se vrati pouze ty kontrakty, ktere splnuji pozadovanou vlastnost
	 * 
	 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
	 *
	 */
	public static enum Feature {
		/**
		 * Kontrakty odebírající zdroje po skončení platnosti
		 */
		DAILY_USAGE_AFTER_CONTRACT_END,

		/**
		 * Kontrakty odebírající zdroje mimo období tarifu
		 */
		DAILY_USAGE_OUT_OF_TARIFF,

		/**
		 * Kontrakty bez typu účtování
		 */
		NO_INVOICE_TYPE,

		/**
		 * Kontrakty bez serveru
		 */
		NO_SERVER,

		/**
		 * Kontrakty s alespon jednim tarifem
		 */
		TARIFF,

		/**
		 * Kontrakty bez tarifu
		 */
		NO_TARIFF,

		/**
		 * Kontrakty se záporným kreditem
		 */
		NEGATIVE_BALANCE,

		/**
		 * Kontrakty s kladným kreditem
		 */
		POSITIVE_BALANCE,

	}

	/**
   * 
   */
	public static enum OrderBy {
		EVIDENCE_NUMBER_ASC,

		EVIDENCE_NUMBER_DESC,

		NAME_ASC,

		NAME_DESC
	}
}
