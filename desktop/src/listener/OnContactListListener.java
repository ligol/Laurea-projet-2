package listener;

public interface OnContactListListener {
    void onConnection(String user, boolean state);

    void onDisconnetion(String user, boolean state);
    
    void onMessage();
}
