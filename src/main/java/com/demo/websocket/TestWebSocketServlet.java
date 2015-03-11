package com.demo.websocket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;
import org.apache.catalina.websocket.WsOutbound;

public class TestWebSocketServlet extends WebSocketServlet {

	/**
* 
*/
	private static final long serialVersionUID = 1L;
	// 存储链接的容器
	private static List<WebSocketMessageInbound> connsList = new ArrayList<WebSocketMessageInbound>();

	@Override
	protected StreamInbound createWebSocketInbound(String subProtocol,
			HttpServletRequest request) {
		// TODO Auto-generated method stub
		return new WebSocketMessageInbound();
	}

	public class WebSocketMessageInbound extends MessageInbound {

		@Override
		protected void onClose(int status) {
			// InitServlet.getSocketList().remove(this);
			super.onClose(status);
			System.out.println("onClose");
			InitServlet.getSocketList().remove(this);
		}

		@Override
		protected void onOpen(WsOutbound outbound) {
			System.out.println("onOpen");
			super.onOpen(outbound);
			InitServlet.getSocketList().add(this);
		}

		@Override
		protected void onBinaryMessage(ByteBuffer message) throws IOException {
			System.out.println("onBinaryMessage");
		}

		@Override
		protected void onTextMessage(CharBuffer message) throws IOException {
			System.out.println("onTextMessage=" + message);
			// this.getWsOutbound().writeTextMessage(CharBuffer.wrap("===="));
			// this.getWsOutbound().writeTextMessage(message);
			// 发送给所有链接的
			for (MessageInbound messageInbound : InitServlet.getSocketList()) {
				CharBuffer buffer = CharBuffer.wrap(message);
				WsOutbound outbound = messageInbound.getWsOutbound();
				outbound.writeTextMessage(buffer);
				outbound.flush();
			}
		}

	}
}