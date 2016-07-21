package com.thesis.domain;

public class Message {
	private String messageContent;
	private User fromUser;
	private User toUser;
	private boolean isReceived;

	public Message() {
	}

	public String getMessageContent() {
		return messageContent;
	}

	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}

	public User getFromUser() {
		return fromUser;
	}

	public void setFromUser(User fromUser) {
		this.fromUser = fromUser;
	}

	public User getToUser() {
		return toUser;
	}

	public void setToUser(User toUser) {
		this.toUser = toUser;
	}

	public boolean isReceived() {
		return isReceived;
	}

	public void setReceived(boolean received) {
		isReceived = received;
	}
}
