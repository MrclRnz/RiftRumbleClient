package de.renz;

import de.renz.rxwebsocket.RxWebSocketClient;

import java.io.*;

public class RiftRumbleClient {

	static String port;
	static String password;

	public static void main(String[] args) throws IOException, InterruptedException {
		RxWebSocketClient client = new RxWebSocketClient("ws://localhost:8080/rx/ws");
		try {
			ProcessBuilder builder = new ProcessBuilder("wmic PROCESS WHERE name='LeagueClientUx.exe' GET commandline");
			builder.redirectErrorStream(true);
			Process process = builder.start();
			InputStream is = process.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));

			System.out.println("Getting port and password of LeagueUX");
			String line = null;
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("LeagueClient")){
					LcuRestApi.port = line.split(":")[2];
					LcuRestApi.password = line.split(":")[3];

					HttpResponse checkClientRunning = LcuRestApi.sendRequest(HttpMethod.GET, "/lol-summoner/v1/current-summoner", null);
					while (!LcuRestApi.isSuccessful(checkClientRunning)){
						System.out.println("League Client is not opened. Retrying in 10s.");
						Thread.sleep(10000);
						checkClientRunning = LcuRestApi.sendRequest(HttpMethod.GET, "/lol-summoner/v1/current-summoner", null);
					}

					client.getSession().getBasicRemote().sendText("Client is running. User = ");

				}
			}
		} catch (IOException e) {
			System.out.println("Error getting port and password for the LCU REST API connection!");
			throw e;
		}
	}
}
