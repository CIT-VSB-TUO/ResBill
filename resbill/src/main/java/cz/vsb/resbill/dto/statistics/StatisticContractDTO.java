/*
 * Copyright (c) 2015 CIT, VŠB-TU Ostrava
 * 
 */
package cz.vsb.resbill.dto.statistics;

import cz.vsb.resbill.dto.contract.ContractDTO;
import cz.vsb.resbill.dto.customer.CustomerDTO;
import cz.vsb.resbill.util.ToStringBuilder;

/**
 * @author Ing. Radek Liebzeit <radek.liebzeit@vsb.cz>
 *
 */
public class StatisticContractDTO extends StatisticDTO {

	/**
   * 
   */
	private static final long serialVersionUID = 1L;

	private ContractDTO contractDTO = null;

	private CustomerDTO customerDTO = null;

	/**
   * 
   */
	@Override
	public String getTitle() {
		return contractDTO.getName();
	}

	/**
	 * @return the contractDTO
	 */
	public ContractDTO getContractDTO() {
		return contractDTO;
	}

	/**
	 * @param contractDTO
	 *          the contractDTO to set
	 */
	public void setContractDTO(ContractDTO contractDTO) {
		this.contractDTO = contractDTO;
	}

	public CustomerDTO getCustomerDTO() {
		return customerDTO;
	}

	public void setCustomerDTO(CustomerDTO customerDTO) {
		this.customerDTO = customerDTO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("contractDTO", contractDTO);
		builder.append("customerDTO", customerDTO);
		builder.append("toString()", super.toString());
		return builder.toString();
	}

}
