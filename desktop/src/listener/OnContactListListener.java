package listener;

public interface OnContactListListener {
    void newConnection(String user, boolean state);

    void newDisconnetion(String user, boolean state);
}
