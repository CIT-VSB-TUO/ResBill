package cz.vsb.resbill.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Address {

	private String descriptiveNumber;
	private String orientationNumber;
	private String street;
	private String cityPart;
	private String city;
	private String country;
	private String zip;

	public String getDescriptiveNumber() {
		return descriptiveNumber;
	}

	public void setDescriptiveNumber(String descriptiveNumber) {
		this.descriptiveNumber = descriptiveNumber;
	}

	public String getOrientationNumber() {
		return orientationNumber;
	}

	public void setOrientationNumber(String orientationNumber) {
		this.orientationNumber = orientationNumber;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCityPart() {
		return cityPart;
	}

	public void setCityPart(String cityPart) {
		this.cityPart = cityPart;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("descriptiveNumber", descriptiveNumber);
		builder.append("orientationNumber", orientationNumber);
		builder.append("street", street);
		builder.append("cityPart", cityPart);
		builder.append("city", city);
		builder.append("country", country);
		builder.append("zip", zip);
		return builder.toString();
	}

}
