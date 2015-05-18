package model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Organizations are entities such as businesses, schools, NGOs,
 * and clubs. They may be affiliated with another organization.
 *
 * @see https://bitpay.com/api#resource-Organizations
 */
public class Organization {

    // Resource id.
    // string, read-only
	private String _id = "";

    // Organization id.
    // string, read-only
	private String _numericId = "";

    // Affiliate organization id.
    // string, read-only
	private String _affiliateOid = "";

    // Business name.
    // string, read/write
	private String _name = "";

    // Street Address.
    // string, read/write
	private String _address1 = "";

    // Apartment or suite number.
    // string, read/write
	private String _address2 = "";

    // City or locality.
    // string, read/write
	private String _locality = "";

    // State or province.
    // string, read/write
	private String _region = "";

    // ZIP or postal code.
    // string, read/write
	private String _postalCode = "";

    // ISO 3166-1 alpha-2 country code.
    // string, read/write
	private String _country = "";

    // Website for customers.
    // string, read/write
	private String _website = "";

    // Support phone contact.
    // string, read/write
	private String _phone = "";

    // Support email contact.
    // string, read/write
	private String _email = "";

    // Legal business name.
    // string, read/write
	private String _legalBusinessName = "";

    // Industry code.
    // string, read/write
	private String _industryCode = "";

    // US tax id.
    // string, read/write
	private String _taxId = "";

    // First name of owner.
    // string, read/write
    private String _givenName = "";

    // Last name of owner.
    // string, read/write
    private String _familyName = "";

    // Label of address or account.
    // string, read/write
    private String _label = "";

    // `BTC` or ISO 4217 3-character currency code.
    // string, read/write
    private String _currency = "BTC";

    // Bitcoin address
    // string, read/write
    private String _bitcoinAddress = "";

    // Bank routing number.
    // string, read/write
    private String _routing = "";

    // Bank account number.
    // string, read/write
    private String _account = "";

    // IBAN account number.
    // string, read/write
    private String _iban = "";

    // SWIFT bank number.
    // string, read/write
    private String _swift = "";

    // Bank sort code.
    // string, read/write
    private String _sort = "";

    // Percentage of net payment.
    // string, read/write
    private double _percent = "";

    // US non-profit status.
    // boolean, read/write
    private boolean _isNonProfit = false;

    // Indicates whether organization
    // has completed account setup.
    // boolean, read only
    private boolean _completedSetup = false;

    /**
     * Sole constructor.
     */
    public Organization() {}

    @JsonIgnore
	public String getId() {
		return _id;
	}

    @JsonIgnore
	public String getNumericId() {
		return _numericId;
	}

    @JsonIgnore
	public String getAffiliateOid() {
		return _affiliateOid;
	}

    @JsonIgnore
	public boolean getCompletedSetup() {
		return _completedSetup;
	}


    @JsonIgnore
	public String getName() {
		return _name;
	}

    @JsonProperty("name")
	public void setName(String _name) {
		this._name = _name;
	}


    @JsonIgnore
	public String getAddress1() {
		return _address1;
	}

    @JsonProperty("address1")
	public void setAddress1(String _address1) {
		this._address1 = _address1;
	}


    @JsonIgnore
	public String getAddress2() {
		return _address2;
	}

    @JsonProperty("address2")
	public void setName(String _address2) {
		this._address2 = _address2;
	}


    @JsonIgnore
	public String getLocality() {
		return _locality;
	}

    @JsonProperty("locality")
	public void setLocality(String _locality) {
		this._locality = _locality;
	}


    @JsonIgnore
	public String getRegion() {
		return _region;
	}

    @JsonProperty("region")
	public void setRegion(String _region) {
		this._region = _region;
	}


    @JsonIgnore
	public String getPostalCode() {
		return _postalCode;
	}

    @JsonProperty("postalCode")
	public void setPostalCode(String _postalCode) {
		this._postalCode = _postalCode;
	}


    @JsonIgnore
	public String getCountry() {
		return _country;
	}
    
    @JsonProperty("country")
	public void setCountry(String _country) {
		this._country = _country;
	}


    @JsonIgnore
	public String getWebsite() {
		return _website;
	}

    @JsonProperty("website")
	public void setWebsite(String _website) {
		this._website = _website;
	}


    @JsonIgnore
	public String getPhone() {
		return _phone;
	}

    @JsonProperty("phone")
	public void setPhone(String _phone) {
		this._phone = _phone;
	}


    @JsonIgnore
	public String getEmail() {
		return _email;
	}

    @JsonProperty("email")
	public void setEmail(String _email) {
		this._email = _email;
	}


    @JsonIgnore
	public String getLegalBusinessName() {
		return _legalBusinessName;
	}

    @JsonProperty("legalBusinessName")
	public void setLegalBusinessName(String _legalBusinessName) {
		this._legalBusinessName = _legalBusinessName;
	}


    @JsonIgnore
	public String getIndustryCode() {
		return _industryCode;
	}

    @JsonProperty("industryCode")
	public void setIndustryCode(String _industryCode) {
		this._industryCode = _industryCode;
	}


    @JsonIgnore
	public String getTaxId() {
		return _taxId;
	}

    @JsonProperty("taxId")
	public void setTaxId(String _taxId) {
		this._taxId = _taxId;
	}


    @JsonIgnore
	public String getGivenName() {
		return _givenName;
	}

    @JsonProperty("givenName")
	public void setGivenName(String _givenName) {
		this._givenName = _givenName;
	}


    @JsonIgnore
	public String getFamilyName() {
		return _familyName;
	}

    @JsonProperty("familyName")
	public void setFamilyName(String _familyName) {
		this._familyName = _familyName;
	}


    @JsonIgnore
	public String getLabel() {
		return _label;
	}

    @JsonProperty("label")
	public void setLabel(String _label) {
		this._label = _label;
	}


    @JsonIgnore
	public String getCurrency() {
		return _currency;
	}

    @JsonProperty("currency")
	public void setCurrency(String _currency) {
		this._currency = _currency;
	}


    @JsonIgnore
	public String getBitcoinAddress() {
		return _bitcoinAddress;
	}

    @JsonProperty("bitcoinAddress")
	public void setBitcoinAddress(String _bitcoinAddress) {
		this._bitcoinAddress = _bitcoinAddress;
	}


    @JsonIgnore
	public String getTaxId() {
		return _taxId;
	}

    @JsonProperty("taxId")
	public void setTaxId(String _taxId) {
		this._taxId = _taxId;
	}


    @JsonIgnore
	public String getRouting() {
		return _routing;
	}

    @JsonProperty("routing")
	public void setRouting(String _routing) {
		this._routing = _routing;
	}


    @JsonIgnore
	public String getAccount() {
		return _account;
	}

    @JsonProperty("account")
	public void setAccount(String _account) {
		this._account = _account;
	}
	

    @JsonIgnore
	public String getIban() {
		return _iban;
	}

    @JsonProperty("iban")
	public void setIban(String _iban) {
		this._iban = _iban;
	}


    @JsonIgnore
	public String getSwift() {
		return _swift;
	}

    @JsonProperty("swift")
	public void setSwift(String _swift) {
		this._swift = _swift;
	}


    @JsonIgnore
	public double getPercent() {
		return _percent;
	}

    @JsonProperty("Percent")
	public void setpercent(double _percent) {
		this._percent = _percent;
	}


    @JsonIgnore
	public boolean getIsNonProfit() {
		return _isNonProfit;
	}

    @JsonProperty("isNonProfit")
	public void setIsNonProfit(boolean _isNonProfit) {
		this._isNonProfit = _isNonProfit;
	}

}
