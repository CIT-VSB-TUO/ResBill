package cz.vsb.resbill.dto;

import java.io.Serializable;

import cz.vsb.resbill.model.Address;

public class AddressDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String inlineFormat;

	private String street;

	private String descriptiveNumber;

	private String orientationNumber;

	private String city;

	private String cityPart;

	private String zip;

	private String country;

	private String houseNumbers;

	private String streetWithNumbers;

	private String cityWithCityPart;

	public AddressDTO(Address a) {
		if (a != null) {
			this.street = a.getStreet();
			this.descriptiveNumber = a.getDescriptiveNumber();
			this.orientationNumber = a.getOrientationNumber();
			this.city = a.getCity();
			this.cityPart = a.getCityPart();
			this.zip = a.getZip();
			this.country = a.getCountry();
			this.houseNumbers = a.getHouseNumbers();
			this.streetWithNumbers = a.getStreetWithNumbers();
			this.cityWithCityPart = a.getCityWithCityPart();
			this.inlineFormat = a.getInlineFormat();
		}
	}

	public String getStreet() {
		return street;
	}

	public String getDescriptiveNumber() {
		return descriptiveNumber;
	}

	public String getOrientationNumber() {
		return orientationNumber;
	}

	public String getCity() {
		return city;
	}

	public String getCityPart() {
		return cityPart;
	}

	public String getZip() {
		return zip;
	}

	public String getCountry() {
		return country;
	}

	public String getHouseNumbers() {
		return houseNumbers;
	}

	public String getStreetWithNumbers() {
		return streetWithNumbers;
	}

	public String getCityWithCityPart() {
		return cityWithCityPart;
	}

	public String getInlineFormat() {
		return inlineFormat;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AddressDTO [");
		builder.append(super.toString());
		builder.append(", inlineFormat=");
		builder.append(inlineFormat);
		builder.append(", street=");
		builder.append(street);
		builder.append(", descriptiveNumber=");
		builder.append(descriptiveNumber);
		builder.append(", orientationNumber=");
		builder.append(orientationNumber);
		builder.append(", city=");
		builder.append(city);
		builder.append(", cityPart=");
		builder.append(cityPart);
		builder.append(", zip=");
		builder.append(zip);
		builder.append(", country=");
		builder.append(country);
		builder.append(", houseNumbers=");
		builder.append(houseNumbers);
		builder.append(", streetWithNumbers=");
		builder.append(streetWithNumbers);
		builder.append(", cityWithCityPart=");
		builder.append(cityWithCityPart);
		builder.append("]");
		return builder.toString();
	}

}
