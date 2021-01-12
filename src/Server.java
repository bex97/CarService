import java.io.*;
import java.net.*;
import java.time.*;
import java.util.ArrayList;
import java.util.concurrent.*;

public class Server {
    ArrayList<ClientHandle> clientHandles;
    int userUniqueID = 1;
    int usersIndex = 1;

    private final ScheduledExecutorService timedExecutorPool = Executors.newScheduledThreadPool(1);
    private final ExecutorService threadPool = Executors.newWorkStealingPool();

    SqlConnection sqlConnection;

    public class ClientHandle implements Runnable {
        String name;
        int index;
        Long time;
        Socket socketMessage;
        BufferedReader bufferedMessageReader;
        InputStreamReader inputMessageStreamReader;
        Boolean isConnected = false;
        PrintWriter printMessageWriter;
        int userUniqueID;

        public ClientHandle(Socket clientSocket, int index, int userUniqueID) {
            try {
                socketMessage = clientSocket;
                printMessageWriter = new PrintWriter(socketMessage.getOutputStream());
                inputMessageStreamReader = new InputStreamReader((socketMessage.getInputStream()));
                bufferedMessageReader = new BufferedReader(inputMessageStreamReader);
                isConnected = true;
                this.index = index;
                printMessageWriter.println(index);
                printMessageWriter.flush();
                this.userUniqueID = userUniqueID;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void run() {
            String message;
            this.index = clientHandles.indexOf(this);
            try {
                while ((message = bufferedMessageReader.readLine()) != null) {
                    requestListener(message, index);
                    clientHandles.get(index).printMessageWriter.println("true");
                    clientHandles.get(index).printMessageWriter.flush();
                }

            } catch (IOException e) {
                this.isConnected = false;
                onDisconnection(clientHandles.indexOf(this));
                clientHandles.remove(this);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (Exception e) {
                clientHandles.get(index).printMessageWriter.println("false");
                clientHandles.get(index).printMessageWriter.flush();
                e.printStackTrace();
            }
        }

    }

    public void requestListener(String message, int index) throws Exception {
        if (message.startsWith("000")) {
            String[] employeeData = message.split("/");
            if (sqlConnection.addEmployee(new Employee(employeeData[1], employeeData[2], employeeData[3],
                    employeeData[4], Integer.parseInt(employeeData[5]), employeeData[6])) == false)
                throw new Exception();

        } else if (message.startsWith("001")) {
            message = message.substring(3);

            // code.. //
        } else if (message.startsWith("002")) {
            message = message.substring(3);

        } else {
            // Wysyłanie klientowi info ze nieznane zapytanko //
            clientHandles.get(index).printMessageWriter.println("Unrecognizable request");
            clientHandles.get(index).printMessageWriter.flush();
        }
    }

    public void connectToDatabase() {
        try {
            sqlConnection = new SqlConnection();
            System.out.println("Connected to database");
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public void run() {
        clientHandles = new ArrayList();

        try {
            ServerSocket serverSocket = new ServerSocket(49152);
            System.out.println("Server online. Server version (1.0.0).");

            System.out.println("Service started!");

            while (true) {
                Thread.sleep(10);
                Socket clientSocket = serverSocket.accept();

                InputStreamReader inputStreamReader = new InputStreamReader(clientSocket.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                connectToDatabase();

                clientHandles.add(new ClientHandle(clientSocket, usersIndex, userUniqueID)); // Dodaj klienta do bazy
                threadPool.execute(clientHandles.get(clientHandles.size() - 1)); // Wykonaj wątek klienta
                clientHandles.get(clientHandles.size() - 1).name = bufferedReader.readLine(); // pobierz imię klienta

                usersIndex += 1;

                System.out.println("User has connected.\t" + LocalDate.now() + ", " + LocalTime.now().withNano(0) + " "
                        + clientSocket.getInetAddress());
                userUniqueID += 1;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void onDisconnection(int index) {
        System.out.println("User has disconnected, " + LocalDate.now() + ", " + LocalTime.now().withNano(0) + ",  "
                + clientHandles.get(index).name + "@" + clientHandles.get(index).index + "\n");
    }

    public static void main(String[] argv) {
        Server server = new Server();
        server.run();
    }
}