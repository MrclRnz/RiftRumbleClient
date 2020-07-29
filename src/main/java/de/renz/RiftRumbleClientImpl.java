package de.renz;

import de.renz.rxwebsocket.shared.RxWebSocketClient;

public class RiftRumbleClientImpl extends RxWebSocketClient {

	public RiftRumbleClientImpl(final String uri){
		super(uri);
	}

	@Override
	public void defineFlow(String message) {

	}
}
