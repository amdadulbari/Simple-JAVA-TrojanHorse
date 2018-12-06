import handlers.ConnectionService;

public class Run {
    public static void main(String[] args) {
        ConnectionService connectionService = new ConnectionService();
        connectionService.initiateConnections();
    }
}
