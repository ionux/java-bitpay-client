package controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

import java.util.Arrays;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import model.Invoice;
import model.Rate;
import model.Rates;
import model.Token;
import model.Organization;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.google.bitcoin.core.ECKey;

public class BitPay {

    private static final String BITPAY_API_VERSION = "2.0.0";
    private static final String BITPAY_PLUGIN_INFO = "BitPay Java Client " + BITPAY_API_VERSION;
    private static final String BITPAY_URL         = "https://bitpay.com/";
    private static final String BITPAY_TEST_URL    = "https://test.bitpay.com/";

    public static final String FACADE_PAYROLL    = "payroll";
    public static final String FACADE_POS        = "pos";
    public static final String FACADE_MERCHANT   = "merchant";
    public static final String FACADE_USER       = "user";
    public static final String FACADE_ONBOARDING = "onboarding";

    // Used for the /orgs endpoint, see:
    // https://bitpay.com/api#references
    public static final String[][] INDUSTRY_CODES = {
        {"BP1621", "Accounting"},
        {"BP8293", "Airlines/Aviation"},
        {"BP4443", "Alternative Dispute Resolution"},
        {"BP4163", "Alternative Medicine"},
        {"BP9372", "Animation"},
        {"BP6770", "Apparel/Fashion"},
        {"BP6823", "Architecture/Planning"},
        {"BP3006", "Arts/Crafts"},
        {"BP2459", "Automotive"},
        {"BP0169", "Aviation/Aerospace"},
        {"BP0683", "Banking/Mortgage"},
        {"BP9810", "Biotechnology/Greentech"},
        {"BP8822", "Bitcoin Mining Hardware"},
        {"BP8452", "Bitcoin Mining Co-op"},
        {"BP1627", "Broadcast Media"},
        {"BP9648", "Building Materials"},
        {"BP7908", "Business Supplies/Equipment"},
        {"BP4175", "Capital Markets/Hedge Fund/Private Equity"},
        {"BP1903", "Chemicals"},
        {"BP2863", "Civic/Social Organization"},
        {"BP6822", "Civil Engineering"},
        {"BP1176", "Commercial Real Estate"},
        {"BP0070", "Computer/Network Security"},
        {"BP1060", "Computer Games"},
        {"BP1683", "Computer Hardware"},
        {"BP0135", "Computer Networking"},
        {"BP4528", "Computer Software/Engineering"},
        {"BP9823", "Construction"},
        {"BP2835", "Consumer Electronics"},
        {"BP0492", "Consumer Goods"},
        {"BP5016", "Consumer Services"},
        {"BP5268", "Cosmetics"},
        {"BP5778", "Currency Exchange"},
        {"BP3715", "Dairy"},
        {"BP0793", "Defense/Space"},
        {"BP8062", "Design"},
        {"BP2962", "Dietary Supplements"},
        {"BP8635", "Education Management"},
        {"BP6150", "E-Learning"},
        {"BP3759", "Electrical/Electronic Manufacturing"},
        {"BP8424", "Entertainment"},
        {"BP2769", "Entertainment - Adult"},
        {"BP9312", "Environmental Services"},
        {"BP9883", "Events Services"},
        {"BP5020", "Executive Office"},
        {"BP5203", "Facilities Services"},
        {"BP8908", "Farming"},
        {"BP1259", "Financial Services"},
        {"BP0483", "Fine Art"},
        {"BP2980", "Fishery"},
        {"BP3789", "Food/Beverages"},
        {"BP1083", "Food Production"},
        {"BP2703", "Fund-Raising"},
        {"BP8130", "Furniture"},
        {"BP0428", "Gambling/Casinos"},
        {"BP3492", "Glass/Ceramics/Concrete"},
        {"BP3331", "Government Administration"},
        {"BP5240", "Government Relations"},
        {"BP2691", "Graphic Design/Web Design"},
        {"BP2699", "Health/Fitness"},
        {"BP6285", "Higher Education/Academia"},
        {"BP6368", "Hospital/Health Care"},
        {"BP5716", "Hospitality"},
        {"BP7639", "Human Resources/HR"},
        {"BP9442", "Import/Export"},
        {"BP5884", "Individual/Family Services"},
        {"BP0462", "Industrial Automation"},
        {"BP9982", "Information Services"},
        {"BP6246", "Information Technology/IT"},
        {"BP7786", "Insurance"},
        {"BP5144", "International Affairs"},
        {"BP7474", "International Trade/Development"},
        {"BP4945", "Internet"},
        {"BP9235", "Investment Banking/Venture"},
        {"BP6174", "Investment Management/Hedge Fund/Private Equity"},
        {"BP5854", "Judiciary"},
        {"BP5614", "Law Enforcement"},
        {"BP0959", "Law Practice/Law Firms"},
        {"BP0875", "Legal Services"},
        {"BP0389", "Legislative Office"},
        {"BP1152", "Leisure/Travel/Tourism"},
        {"BP1671", "Libraries"},
        {"BP0068", "Logistics/Supply Chain"},
        {"BP2686", "Luxury Goods/Jewelry"},
        {"BP8110", "Machinery"},
        {"BP8883", "Management Consulting"},
        {"BP6520", "Maritime"},
        {"BP2641", "Market Research"},
        {"BP2136", "Marketing/Advertising/Sales"},
        {"BP5592", "Mechanical or Industrial Engineering"},
        {"BP8891", "Media Production"},
        {"BP4115", "Medical Devices/Equipment"},
        {"BP2970", "Medical Practice"},
        {"BP4178", "Mental Health Care"},
        {"BP1043", "Military"},
        {"BP4244", "Motion Pictures/Film"},
        {"BP2215", "Museums/Institutions"},
        {"BP0533", "Music"},
        {"BP1664", "Nanotechnology"},
        {"BP4929", "Newspapers/Journalism"},
        {"BP5293", "Non-Profit/Volunteering"},
        {"BP1226", "Novelties - Adult"},
        {"BP1759", "Oil/Energy/Solar/Greentech"},
        {"BP1376", "Online Publishing"},
        {"BP5916", "Outsourcing/Offshoring"},
        {"BP5206", "Package/Freight Delivery"},
        {"BP6850", "Packaging/Containers"},
        {"BP4340", "Paper/Forest Products"},
        {"BP7841", "Performing Arts"},
        {"BP7408", "Prepaid Cards/Gift Cards"},
        {"BP7376", "Pharmaceuticals"},
        {"BP8014", "Philanthropy"},
        {"BP4387", "Photography"},
        {"BP5475", "Plastics"},
        {"BP2310", "Political Organization"},
        {"BP0088", "Precious Metals"},
        {"BP7297", "Primary/Secondary Education"},
        {"BP1099", "Printing"},
        {"BP5901", "Professional Training"},
        {"BP9953", "Program Development"},
        {"BP0937", "Public Policy"},
        {"BP1761", "Public Relations/PR"},
        {"BP7033", "Public Safety"},
        {"BP3275", "Publishing Industry"},
        {"BP1539", "Railroad Manufacture"},
        {"BP8414", "Ranching"},
        {"BP9091", "Real Estate/Mortgage"},
        {"BP0007", "Recreational Facilities/Services"},
        {"BP2864", "Religious Institutions"},
        {"BP9264", "Renewables/Environment"},
        {"BP6653", "Research Industry"},
        {"BP2938", "Restaurants"},
        {"BP6277", "Retail Industry"},
        {"BP3520", "Security/Investigations"},
        {"BP1590", "Semiconductors"},
        {"BP1104", "Shipbuilding"},
        {"BP7148", "Sporting Goods"},
        {"BP3345", "Sports"},
        {"BP3135", "Staffing/Recruiting"},
        {"BP2620", "Supermarkets"},
        {"BP6279", "Telecommunications"},
        {"BP7010", "Textiles"},
        {"BP5077", "Think Tanks"},
        {"BP6741", "Tobacco"},
        {"BP3367", "Translation/Localization"},
        {"BP5132", "Transportation"},
        {"BP4134", "Utilities"},
        {"BP2191", "Venture Capital/VC"},
        {"BP4737", "Veterinary"},
        {"BP3527", "Video on Demand"},
        {"BP7456", "Warehousing"},
        {"BP0126", "Wholesale"},
        {"BP3612", "Wine/Spirits"},
        {"BP0514", "Wireless"},
        {"BP8901", "Writing/Editing"},
    };

    private HttpClient _httpClient = null;
    private String _baseUrl        = BITPAY_URL;
    private ECKey _ecKey           = null;
    private String _identity       = "";
    private long _nonce            = new Date().getTime();
    private boolean _disableNonce  = false;
    private String _clientName     = "";
    private Hashtable<String, String> _tokenCache; // {facade, token}
    
    /**
     * Constructor for use the client name and target server URL strings.
     *
     * @param  clientName      The label for this client.
     * @param  envUrl          The target server URL.
     * @throws BitPayException
     */
    public BitPay(String clientName, String envUrl) throws BitPayException
    {
        if (clientName.equals(BITPAY_PLUGIN_INFO)) {
            try {
                clientName += " on " + java.net.InetAddress.getLocalHost();
            } catch (UnknownHostException e) {
                clientName += " on unknown host";
            }
        }

        // Eliminate special characters from the client
        // name (used as a token label).  Trim to 60 chars.
        _clientName = clientName.replaceAll("[^a-zA-Z0-9_ ]", "_");

        if (_clientName.length() > 60) {
            _clientName = _clientName.substring(0, 60);
        }

        _baseUrl = envUrl;
        _httpClient = HttpClientBuilder.create().build();

        try {
            this.initKeys();
        } catch (IOException e) {
            throw new BitPayException("Error: failed to intialize public/private key pair\n" + e.getMessage());
        }

        this.deriveIdentity();
        this.tryGetAccessTokens();
    }

    /**
     * Constructor for use with the client name string.
     *
     * @param  clientName      The label for this client.
     * @throws BitPayException
     */
    public BitPay(String clientName) throws BitPayException
    {
        this(clientName, BITPAY_URL);
    }

    /**
     * Constructor.
     *
     * @throws BitPayException
     */
    public BitPay() throws BitPayException
    {
        this(BITPAY_PLUGIN_INFO, BITPAY_URL);
    }

    /**
     * Constructor for use with the keys, client name and target server URL strings.
     *
     * @param  ecKey           An elliptical curve key.
     * @param  clientName      The label for this client.
     * @param  envUrl          The target server URL.
     * @throws BitPayException
     */
    public BitPay(ECKey ecKey, String clientName, String envUrl) throws BitPayException
    {
        _ecKey = ecKey;

        this.deriveIdentity();

        if (clientName.equals(BITPAY_PLUGIN_INFO)) {
            try {
                clientName += " on " + java.net.InetAddress.getLocalHost();
            } catch (UnknownHostException e) {
                clientName += " on unknown host";
            }
        }

        // Eliminate special characters from the client
        // name (used as a token label).  Trim to 60 chars.
        _clientName = clientName.replaceAll("[^a-zA-Z0-9_ ]", "_");

        if (_clientName.length() > 60) {
            _clientName = _clientName.substring(0, 60);
        }

        _baseUrl = envUrl;
        _httpClient = HttpClientBuilder.create().build();

        this.tryGetAccessTokens();
    }

    /**
     * Constructor for use with the keys and client name string.
     *
     * @param  ecKey           An elliptical curve key.
     * @param  clientName      The label for this client.
     * @throws BitPayException
     */
    public BitPay(ECKey ecKey, String clientName) throws BitPayException
    {
        this(ecKey, clientName, BITPAY_URL);
    }

    /**
     * Constructor for use with just the keys.
     *
     * @param  ecKey           An elliptical curve key.
     * @throws BitPayException
     */
    public BitPay(ECKey ecKey) throws BitPayException
    {
        this(ecKey, BITPAY_PLUGIN_INFO, BITPAY_URL);
    }

    /**
     * Returns the identity property string.
     *
     * @return _identity The identity property string.
     */
    public String getIdentity()
    {
        return _identity;
    }

    /**
     * Used to check if nonces are disabled or not.
     *
     * @return _disableNonce Boolean value to check for nonce usage.
     */
    public boolean getDisableNonce()
    {
        return _disableNonce;
    }

    /**
     * Used to disable/enable nonce usage.
     *
     * @param value Boolean value to disable/enable nonce usage.
     */
    public void setDisableNonce(boolean value)
    {
        _disableNonce = value;
    }

    /**
     * Pairs a client with a merchant accound and stores
     * all returned tokens to the token cache. See:
     * https://bitpay.com/api#getting-access
     *
     * @param  pairingCode     The pairing code generated for your account.
     * @throws BitPayException
     */
    public void authorizeClient(String pairingCode) throws BitPayException
    {
        Token token = new Token();
        token.setId(_identity);
        token.setGuid(this.getGuid());
        token.setNonce(getNextNonce());
        token.setPairingCode(pairingCode);
        token.setLabel(_clientName);

        ObjectMapper mapper = new ObjectMapper();

        String json;

        try {
            json = mapper.writeValueAsString(token);
        } catch (JsonProcessingException e) {
            throw new BitPayException("Error - failed to serialize Token object : " + e.getMessage());
        }

        HttpResponse response = this.post("tokens", json);

        List<Token> tokens;

        try {
            tokens = Arrays.asList(mapper.readValue(this.responseToJsonString(response), Token[].class));
        } catch (JsonProcessingException e) {
            throw new BitPayException("Error - failed to deserialize BitPay server response (Tokens) : " + e.getMessage());
        } catch (IOException e) {
            throw new BitPayException("Error - failed to deserialize BitPay server response (Tokens) : " + e.getMessage());
        }

        for (Token t : tokens) {
            _tokenCache.put(t.getFacade(), t.getValue());
        }
    }

    /**
     * Generates a pairing code for a merchant account
     * for the requested facade type.
     *
     * @param  facade          Requested facade type.
     * @return pairingCode     The returned pairing code.
     * @throws BitPayException
     */
    public String requestClientAuthorization(String facade) throws BitPayException
    {
        Token token = new Token();
        token.setId(_identity);
        token.setGuid(this.getGuid());
        token.setNonce(getNextNonce());
        token.setFacade(facade);
        token.setCount(1);
        token.setLabel(_clientName);

        ObjectMapper mapper = new ObjectMapper();

        String json;

        try {
            json = mapper.writeValueAsString(token);
        } catch (JsonProcessingException e) {
            throw new BitPayException("Error - failed to serialize Token object : " + e.getMessage());
        }

        HttpResponse response = this.post("tokens", json);
        
        List<Token> tokens;

        try {
            tokens = Arrays.asList(mapper.readValue(this.responseToJsonString(response), Token[].class));

            // Expecting a single token resource.
            if (tokens.size() != 1) {
                throw new BitPayException("Error - failed to get token resource; expected 1 token, got " + tokens.size());
            }

        } catch (JsonProcessingException e) {
            throw new BitPayException("Error - failed to deserialize BitPay server response (Tokens) : " + e.getMessage());
        } catch (IOException e) {
            throw new BitPayException("Error - failed to deserialize BitPay server response (Tokens) : " + e.getMessage());
        }

        _tokenCache.put(tokens.get(0).getFacade(), tokens.get(0).getValue());

        return tokens.get(0).getPairingCode();
    }

    /**
     * Checks to see if the token cache contains a token
     * for the requested facade, i.e. is authorized.
     *
     * @param  facade  Requested facade type.
     * @return boolean Whether or not the token cache has the key.
     */
    public boolean clientIsAuthorized(String facade)
    {
        return _tokenCache.containsKey(facade);
    }

    /**
     * Generates a new BitPay invoice for the merchant account.
     *
     * @param  invoice         Instance of an invoice object.
     * @param  facade          The facade type to use.
     * @return invoice         The updated invoice object.
     * @throws BitPayException
     */
    public Invoice createInvoice(Invoice invoice, String facade) throws BitPayException
    {
        invoice.setToken(this.getAccessToken(facade));
        invoice.setGuid(this.getGuid());
        invoice.setNonce(this.getNextNonce());

        ObjectMapper mapper = new ObjectMapper();

        String json;

        try {
            json = mapper.writeValueAsString(invoice);
        } catch (JsonProcessingException e) {
            throw new BitPayException("Error - failed to serialize Invoice object : " + e.getMessage());
        }

        HttpResponse response = this.postWithSignature("invoices", json);        

        try {
            invoice = mapper.readerForUpdating(invoice).readValue(this.responseToJsonString(response));
        } catch (JsonProcessingException e) {
            throw new BitPayException("Error - failed to deserialize BitPay server response (Invoice) : " + e.getMessage());
        } catch (IOException e) {
            throw new BitPayException("Error - failed to deserialize BitPay server response (Invoice) : " + e.getMessage());
        }

        return invoice;
    }

    /**
     * Generates a new BitPay invoice for the merchant account.
     * Uses the merchant facade by default.
     *
     * @param  invoice         Instance of an invoice object.
     * @return invoice         The updated invoice object.
     * @throws BitPayException
     */
    public Invoice createInvoice(Invoice invoice) throws BitPayException
    {
        return this.createInvoice(invoice, FACADE_MERCHANT);
    }

    /**
     * Retrieves the details of an invoice.
     *
     * @param  invoiceId       The BitPay invoice id.
     * @return i               The updated invoice object.
     * @throws BitPayException
     */
    public Invoice getInvoice(String invoiceId) throws BitPayException
    {
        HttpResponse response = this.get("invoices/" + invoiceId);

        Invoice i;

        try {
            i = new ObjectMapper().readValue(this.responseToJsonString(response), Invoice.class);
        } catch (JsonProcessingException e) {
            throw new BitPayException("Error - failed to deserialize BitPay server response (Invoice) : " + e.getMessage());
        } catch (IOException e) {
            throw new BitPayException("Error - failed to deserialize BitPay server response (Invoice) : " + e.getMessage());
        }

        return i;
    }

    /**
     * Retrieves a list of all invoices for the calling merchant.
     *
     * @param  dateStart        The search start date.
     * @param  dateEnd          The search end date.
     * @return invoices         A List object of all invoices found.
     * @throws BitPayException
     */
    public List<Invoice> getInvoices(String dateStart, String dateEnd) throws BitPayException
    {
        Hashtable<String, String> parameters = this.getParams();

        parameters.put("token", this.getAccessToken(FACADE_MERCHANT));
        parameters.put("dateStart", dateStart);
        parameters.put("dateEnd", dateEnd);

        HttpResponse response = this.get("invoices", parameters);

        List<Invoice> invoices;

        try {
            invoices = Arrays.asList(new ObjectMapper().readValue(this.responseToJsonString(response), Invoice[].class));
        } catch (JsonProcessingException e) {
            throw new BitPayException("Error - failed to deserialize BitPay server response (Invoices) : " + e.getMessage());
        } catch (IOException e) {
            throw new BitPayException("Error - failed to deserialize BitPay server response (Invoices) : " + e.getMessage());
        }

        return invoices;
    }

    /**
     * Retrieves the current exchange rates from BitPay.
     *
     * @return Rates           A Rates object of updated exchange rate information.
     * @throws BitPayException
     */
    public Rates getRates() throws BitPayException
    {
        HttpResponse response = this.get("rates");

        List<Rate> rates;

        try {
            rates = Arrays.asList(new ObjectMapper().readValue(this.responseToJsonString(response), Rate[].class));
        } catch (JsonProcessingException e) {
            throw new BitPayException("Error - failed to deserialize BitPay server response (Rates) : " + e.getMessage());
        } catch (IOException e) {
            throw new BitPayException("Error - failed to deserialize BitPay server response (Rates) : " + e.getMessage());
        }

        return new Rates(rates, this);
    }

    /**
     * Loads an existing EC keypair or generates new pair.
     *
     * @throws IOException
     */
    private void initKeys() throws IOException
    {
        if (KeyUtils.privateKeyExists()) {
            _ecKey = KeyUtils.loadEcKey();

            // Alternatively, load your private key from a location you specify.
            // _ecKey = KeyUtils.createEcKeyFromHexStringFile("C:\\Users\\key.priv");

        } else {
            _ecKey = KeyUtils.createEcKey();
            KeyUtils.saveEcKey(_ecKey);
        }
    }

    /**
     * Creates a SIN from the public EC key.
     *
     * @throws IllegalArgumentException
     */
    private void deriveIdentity() throws IllegalArgumentException
    {
        // Identity in this implementation is defined to be the SIN.
        _identity = KeyUtils.deriveSIN(_ecKey);
    }

    /**
     * Returns the next nonce value to use for API calls,
     * if nonce usage is not disabled.
     *
     * @return _nonce A new nonce value
     */
    private long getNextNonce()
    {
        // Nonce must be 0 when it has been disabled.
        // (0 value prevents serialization)

        if (!getDisableNonce()) {
            _nonce++;
        } else {
            _nonce = 0;
        }

        return _nonce;
    }

    /**
     * Populates the token cache from the JSON results
     * of an API call to the gateway.
     *
     * @param  response        The gateway response.
     * @return _tokenCache     A Hashtable of the token type & value
     * @throws BitPayException
     */
    private Hashtable<String, String> responseToTokenCache(HttpResponse response) throws BitPayException
    {
        // The response is expected to be an array of
        // key/value pairs (facade name = token).
        String json = this.responseToJsonString(response);

        try {
            // Remove the array context so
            // we can map to a hashtable.
            json = json.replaceAll("\\[", "");
            json = json.replaceAll("\\]", "");

            if (json.length() > 0) {
                _tokenCache = new ObjectMapper().readValue(json, new TypeReference<Hashtable<String,String>>(){});
            }

        } catch (JsonProcessingException e) {
            throw new BitPayException("Error - failed to deserialize BitPay server response (Token array) : " + e.getMessage());
        } catch (IOException e) {
            throw new BitPayException("Error - failed to deserialize BitPay server response (Token array) : " + e.getMessage());
        }

        return _tokenCache;
    }

    /**
     * Reinitialize the token cache object.
     */
    private void clearAccessTokenCache() 
    {
        _tokenCache = new Hashtable<String, String>();
    }

    /**
     * Attempt to get access tokens for this client identity.
     *
     * @return boolean         Was at least one token returned?
     * @throws BitPayException
     */
    private boolean tryGetAccessTokens() throws BitPayException
    {
        try {
            // Success if at least one access token was returned.
            return this.getAccessTokens() > 0;

        } catch (BitPayException ex) {

            // If the error states that the identity is
            // invalid then this client has not been
            // registered with the BitPay account.
            if (ex.getMessage().contains("Unauthorized sin")) {
                this.clearAccessTokenCache();
                return false;
            } else {
                // Propagate all other errors.
                throw ex;
            }
        }
    }

    /**
     * Populates the token cache with the gateway response.
     *
     * @return integer         Size of the token cache.
     * @throws BitPayException
     */
    private int getAccessTokens() throws BitPayException
    {
        this.clearAccessTokenCache();

        Hashtable<String, String> parameters = this.getParams();
        HttpResponse response = this.get("tokens", parameters);

        _tokenCache = responseToTokenCache(response);

        return _tokenCache.size();
    }

    /**
     * Checks token cache for a token matching the facade string.
     *
     * @param  facade          The requested facade string.
     * @return token           The matching token value string.
     * @throws BitPayException
     */
    private String getAccessToken(String facade) throws BitPayException
    {
        if (!_tokenCache.containsKey(facade)) {
            throw new BitPayException("Error: You do not have access to facade: " + facade);
        }

        return _tokenCache.get(facade);
    }

    /**
     * Populates the params hash table object with a new nonce.
     *
     * @return params          The updated params Hashtable object
     * @throws BitPayException
     */
    private Hashtable<String, String> getParams() 
    {
        Hashtable<String, String> params = new Hashtable<String, String>();

        params.put("nonce", getNextNonce() + "");

        return params;
    }

    /**
     * Executes an HTTP GET request against the gateway.
     *
     * @param  uri             The server address to call.
     * @param  parameters      A hash table of parameters for the call.
     * @return HttpResponse    The response object from the gateway.
     * @throws BitPayException
     */
    private HttpResponse get(String uri, Hashtable<String, String> parameters) throws BitPayException
    {
        try {

            String fullURL = _baseUrl + uri;
            HttpGet get = new HttpGet(fullURL);

            if (parameters != null) {
                fullURL += "?";

                for (String key : parameters.keySet()) {
                    fullURL += key + "=" + parameters.get(key) + "&";
                }

                fullURL = fullURL.substring(0,fullURL.length() - 1);

                get.setURI(new URI(fullURL));

                String signature = KeyUtils.sign(_ecKey, fullURL);

                get.addHeader("x-bitpay-plugin-info", BITPAY_PLUGIN_INFO);
                get.addHeader("x-accept-version", BITPAY_API_VERSION);
                get.addHeader("x-signature", signature);
                get.addHeader("x-identity", KeyUtils.bytesToHex(_ecKey.getPubKey()));
            }

            return _httpClient.execute(get);

        } catch (URISyntaxException e) {
            throw new BitPayException("Error: GET failed\n" + e.getMessage());
        } catch (ClientProtocolException e) {
            throw new BitPayException("Error: GET failed\n" + e.getMessage());
        } catch (IOException e) {
            throw new BitPayException("Error: GET failed\n" + e.getMessage());
        }
    }

    /**
     * Executes an HTTP GET request against the gateway without
     * any parameters.
     *
     * @param  uri             The server address to call.
     * @return HttpResponse    The response object from the gateway.
     * @throws BitPayException
     */
    private HttpResponse get(String uri) throws BitPayException
    {
        return this.get(uri, null);
    }

    /**
     * Executes an HTTP POST request against the gateway with parameters
     * and an additional ECDSA signature. This is required for all non-
     * public resource API calls.
     *
     * @param  uri               The server address to call.
     * @param  json              A JSON string of parameters for the call.
     * @param  signatureRequired Whether or not to use a signature.
     * @return HttpResponse      The response object from the gateway.
     * @throws BitPayException
     */
    private HttpResponse post(String uri, String json, boolean signatureRequired) throws BitPayException 
    {
        try {            
            HttpPost post = new HttpPost(_baseUrl + uri);

            post.setEntity(new ByteArrayEntity(json.toString().getBytes("UTF8")));

            if (signatureRequired) {
                String signature = KeyUtils.sign(_ecKey, _baseUrl + uri + json);

                post.addHeader("x-signature", signature);
                post.addHeader("x-identity", KeyUtils.bytesToHex(_ecKey.getPubKey()));
            }

            post.addHeader("x-accept-version", BITPAY_API_VERSION);
            post.addHeader("x-bitpay-plugin-info", BITPAY_PLUGIN_INFO);
            post.addHeader("Content-Type","application/json");

            return _httpClient.execute(post);

        } catch (UnsupportedEncodingException e) {
            throw new BitPayException("Error: POST failed\n" + e.getMessage());
        } catch (ClientProtocolException e) {
            throw new BitPayException("Error: POST failed\n" + e.getMessage());
        } catch (IOException e) {
            throw new BitPayException("Error: POST failed\n" + e.getMessage());
        }
    }

    /**
     * Executes an HTTP POST request against the gateway with parameters.
     *
     * @param  uri             The server address to call.
     * @param  json            A JSON string of parameters for the call.
     * @return HttpResponse    The response object from the gateway.
     * @throws BitPayException
     */
    private HttpResponse post(String uri, String json) throws BitPayException 
    {
        return this.post(uri, json, false);
    }

    /**
     * Executes an HTTP POST request against the gateway with parameters
     * and an additional ECDSA signature. This is required for all non-
     * public resource API calls.
     *
     * @param  uri             The server address to call.
     * @param  json            A JSON string of parameters for the call.
     * @return HttpResponse    The response object from the gateway.
     * @throws BitPayException
     */
    private HttpResponse postWithSignature(String uri, String json) throws BitPayException 
    {
        return this.post(uri, json, true);
    }

    /**
     * Returns the string value for the JSON payload of the HTTP response object.
     *
     * @param  response        The unprocessed HTTP response object.
     * @return jsonString      The JSON string value from the response.
     * @throws BitPayException
     */
    private String responseToJsonString(HttpResponse response) throws BitPayException 
    {
        if (response == null) {
            throw new BitPayException("Error: HTTP response is null");
        }

        try {
            // Get the JSON string from the response.
            HttpEntity entity = response.getEntity();

            String jsonString;

            jsonString = EntityUtils.toString(entity, "UTF-8");

            ObjectMapper mapper = new ObjectMapper();

            JsonNode rootNode = mapper.readTree(jsonString);
            JsonNode node = rootNode.get("error");

            if (node != null) {
                throw new BitPayException("Error: " + node.asText());
            }

            node = rootNode.get("errors");

            if (node != null) {
                String message = "Multiple errors:";

                if (node.isArray()) {
                    for (final JsonNode errorNode : node) {
                        message += "\n" + errorNode.asText();
                    }

                    throw new BitPayException(message);
                }
            }

            node = rootNode.get("data");

            if (node != null) {
                jsonString = node.toString();
            }

            return jsonString;

        } catch (ParseException e) {
            throw new BitPayException("Error - failed to retrieve HTTP response body : " + e.getMessage());
        } catch (JsonMappingException e) {
            throw new BitPayException("Error - failed to parse json response to map : " + e.getMessage());
        } catch (IOException e) {
            throw new BitPayException("Error - failed to retrieve HTTP response body : " + e.getMessage());
        }
    }

    /**
     * Generates & returns a new random globally uniqe identifier.
     * See: http://en.wikipedia.org/wiki/Globally_unique_identifier
     *
     * @return string The new GUID value.
     */
    private String getGuid() 
    {
        int Min = 0;
        int Max = 99999999;

        return Min + (int)(Math.random() * ((Max - Min) + 1)) + "";
    }
}
