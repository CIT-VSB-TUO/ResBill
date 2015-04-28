package cz.vsb.resbill.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Address implements Serializable {

	private static final long serialVersionUID = -2979179809235914411L;

	@Column(name = "desc_number", length = 15)
	private String descriptiveNumber;

	@Column(name = "orient_number", length = 8)
	private String orientationNumber;

	@Column(name = "street", length = 100)
	private String street;

	@Column(name = "city_part", length = 100)
	private String cityPart;

	@Column(name = "city", length = 100)
	private String city;

	@Column(name = "country", length = 100)
	private String country;

	@Column(name = "zip", length = 15)
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
		StringBuilder builder = new StringBuilder();
		builder.append("Address [");
		builder.append(super.toString());
		builder.append(", descriptiveNumber=");
		builder.append(descriptiveNumber);
		builder.append(", orientationNumber=");
		builder.append(orientationNumber);
		builder.append(", street=");
		builder.append(street);
		builder.append(", cityPart=");
		builder.append(cityPart);
		builder.append(", city=");
		builder.append(city);
		builder.append(", country=");
		builder.append(country);
		builder.append(", zip=");
		builder.append(zip);
		builder.append("]");
		return builder.toString();
	}

}
