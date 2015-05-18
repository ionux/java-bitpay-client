package model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Organization {

	private String _id;
	private String _numericId;
	private String _affiliateOid;
	private String _name;
	private String _address1;
	private String _address2;
	private String _locality;
	private String _region;
	private String _postalCode;
	private String _country;
	private String _website;
	private String _phone;
	private String _email;
	private String _legalBusinessName;
	private String _industryCode;
	private String _taxId;
    private String _
    private String _
    private String _
    private String _
    private String _
    public Rate() {}
    
    @JsonIgnore
	public String getName() {
		return _name;
	}
    
    @JsonProperty("name")
	public void setName(String _name) {
		this._name = _name;
	}

    @JsonIgnore
	public String getCode() {
		return _code;
	}
    
    @JsonProperty("code")
	public void setCode(String _code) {
		this._code = _code;
	}

    @JsonIgnore
	public double getValue() {
		return _value;
	}
    
    @JsonProperty("rate")
	public void setValue(double _value) {
		this._value = _value;
	}

}
