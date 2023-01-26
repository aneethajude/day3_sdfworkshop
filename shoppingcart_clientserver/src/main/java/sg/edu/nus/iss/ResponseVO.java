package sg.edu.nus.iss;

import java.io.Serializable;
import java.util.List;

public class ResponseVO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String responseStatus;
	private String responseMessage;
	private List<String> cartItems;
	public String getResponseStatus() {
		return responseStatus;
	}
	public void setResponseStatus(String responseStatus) {
		this.responseStatus = responseStatus;
	}
	public String getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}
	public List<String> getCartItems() {
		return cartItems;
	}
	public void setCartItems(List<String> cartItems) {
		this.cartItems = cartItems;
	}

}
