package de.renz;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class LcuRestApi {

	public static String port;
	public static String password;

	public static boolean isSuccessful(HttpResponse response){
		return response.statusCode == 200;
	}

	private static HttpURLConnection setAuthHeader(HttpURLConnection con){
		String auth = "riot" + ":" + password;
		byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
		String authHeaderValue = "Basic " + new String(encodedAuth);
		con.setRequestProperty("Authorization", authHeaderValue);

		return con;
	}

	public static HttpResponse sendRequest(HttpMethod method, String endpoint, String body) throws IOException {
		int statusCode = -1;
		URL url = new URL("http://localhost:" + port + endpoint);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod(method.toString());
		con = setAuthHeader(con);

		if(body != null){
			con.addRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("Content-Length", Integer.toString(body.length()));
			con.getOutputStream().write(body.getBytes("UTF8"));
		}

		int status = con.getResponseCode();

		Reader streamReader = null;
		BufferedReader in = null;
		StringBuilder content = new StringBuilder();
		try {
			if (status > 299) {
				streamReader = new InputStreamReader(con.getErrorStream());
			} else {
				streamReader = new InputStreamReader(con.getInputStream());
			}

			in = new BufferedReader(streamReader);
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				content.append(inputLine);
			}
		} finally {
			streamReader.close();
			in.close();
		}

		return new HttpResponse(statusCode, content.toString());
	}

}
