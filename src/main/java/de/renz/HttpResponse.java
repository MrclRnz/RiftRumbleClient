package de.renz;

public class HttpResponse {
	public final int statusCode;
	public final String responseMessage;

	public HttpResponse(int statusCode, String responseMessage){
		this.statusCode = statusCode;
		this.responseMessage = responseMessage;
	}
}
