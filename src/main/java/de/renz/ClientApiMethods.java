package de.renz;

import com.stirante.lolclient.ClientApi;
import com.stirante.lolclient.ClientConnectionListener;
import de.renz.rxwebsocket.shared.WorkflowSteps;
import generated.LolLobbyLobbyCustomGameConfiguration;
import generated.LolLobbyLobbyCustomGameLobby;
import generated.LolLobbyLobbyInvitation;
import generated.LolLobbyLobbyInvitationState;

import java.io.IOException;
import java.util.List;

public class ClientApiMethods {
	static ClientApi api = new ClientApi();
	static RiftRumbleClientImpl client = new RiftRumbleClientImpl("ws://localhost:8080/rx/ws");

	static {
		api.addClientConnectionListener(new ClientConnectionListener() {
			@Override
			public void onClientConnected() {
				try {
					client.getSession().getBasicRemote().sendText(Integer.toString(WorkflowSteps.STEP_CHECK_CLIENT_PROCESS));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onClientDisconnected() { }
		});
	}

	public static void createLobby(){
		LolLobbyLobbyCustomGameLobby customLobby = new LolLobbyLobbyCustomGameLobby();
		customLobby.lobbyName = "RiftRumble";
		customLobby.lobbyPassword = "riftrumble";


		LolLobbyLobbyCustomGameConfiguration config = new LolLobbyLobbyCustomGameConfiguration();
		config.gameMode = "CLASSIC";
		config.mapId = 11;
		config.teamSize = 5;
		customLobby.configuration = config;
		try{
			customLobby = api.executePost("lol-lobby/v2/lobby", customLobby, LolLobbyLobbyCustomGameLobby.class);
		} catch(IOException e){
			System.out.println("Error creating custom game lobby: " + e.getMessage());
		}

		if(customLobby.gameId != null && customLobby.gameId != 0){

		} else {

		}
	}

	public static void invitePlayers(String owner, List<String> playersToBeInvited){

		for (String player : playersToBeInvited){
			LolLobbyLobbyInvitation invitation = new LolLobbyLobbyInvitation();
			invitation.fromSummonerName = owner;
			invitation.toSummonerName = player;
			try {
				api.executePost("/lol-lobby/v2/lobby/invitations", invitation, LolLobbyLobbyInvitation.class);
			} catch(IOException e){
				System.out.println("Error inviting player " + player + ": " + e.getMessage());
			}
		}

	}

	public static void acceptInvitation(){
		try {
			int invitationId = 0; // api.executeGet("GET /lol-lobby/v2/received-invitations", LolLobbyLobbyInvitation.class)

			LolLobbyLobbyInvitation invitation = api.executePost("/lol-lobby/v2/received-invitations/" + invitationId + "/accept", LolLobbyLobbyInvitation.class);
			if(invitation.state == LolLobbyLobbyInvitationState.JOINED){
				client.getSession().getBasicRemote().sendText(WorkflowSteps.STEP_ACCEPT_INVITATION + ": Completed");
			} else {
				client.getSession().getBasicRemote().sendText(WorkflowSteps.STEP_ACCEPT_INVITATION + ": Failure");
			}
		} catch(IOException e){
			System.out.println("Error accepting invitation" + e.getMessage());
		}

	}

	public static void startChampSelect(){

	}

	public static void createRunepage(){

	}

	public static void selectRunepage(){

	}

	public static void selectSummoners(){

	}
}
