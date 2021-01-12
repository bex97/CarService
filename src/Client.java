import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.*;
import java.util.regex.Pattern;

public class Client {
    String ip = "127.0.0.1";
    int port = 49152;

    Pattern pattern = Pattern.compile("^[a-zA-Z0-9]+$");
    ArrayList<ClientList> connectedList = new ArrayList<>();

    private final ExecutorService threadPool = Executors.newFixedThreadPool(1);
    private final ScheduledExecutorService timedExecutorPool = Executors.newScheduledThreadPool(1);

    public class ClientList {
        String name;
        int index;

        ClientList(String name, int index) {
            this.name = name;
            this.index = index;
        }
    }

    public class ClientHandle {
        String name;
        Socket socket;
        BufferedReader bufferedReader;
        PrintWriter printWriter;
        Boolean isConnected = false;
        InputStreamReader inputStreamReader;
        int userUniqueId;

        public void showStatus() {
            System.out.println(
                    "You are connected: " + this.isConnected + "\nShown as: " + this.name + "#" + this.userUniqueId);
        }
    }

    ClientHandle clientHandle = new ClientHandle();

    public void connectToServer(String ip, int port) {
        if (!clientHandle.isConnected) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                clientHandle.showStatus();
                clientHandle.socket = new Socket(ip, port);
                clientHandle.isConnected = true;

                clientHandle.inputStreamReader = new InputStreamReader(clientHandle.socket.getInputStream());
                clientHandle.bufferedReader = new BufferedReader(clientHandle.inputStreamReader);
                clientHandle.printWriter = new PrintWriter(clientHandle.socket.getOutputStream());
                // threadPool.execute(new WarningReceiver());
                clientHandle.isConnected = true;

                whatIsYourName();
                clientHandle.showStatus();

                System.out.println("Connected with server");

            } catch (Exception e) {
                System.out.println("Can't connect with server");

                clientHandle.socket = null;
                clientHandle.isConnected = false;
                clientHandle.printWriter = null;
                clientHandle.bufferedReader = null;
                clientHandle.inputStreamReader = null;
                clientHandle.isConnected = false;
            }
        } else {
            System.out.println("You are already connected.");
        }
    }

    public void whatIsYourName() {
        clientHandle.printWriter.println(clientHandle.name);
        clientHandle.printWriter.flush();

        try {
            clientHandle.userUniqueId = Integer.decode(clientHandle.bufferedReader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class checkConnection implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (clientHandle.isConnected) {
                String message = "000";
                message += System.nanoTime();
                clientHandle.printWriter.println(message);
                clientHandle.printWriter.flush();
            } else {
                System.out.println("Check your connection with server.");
            }
        }
    }

    public boolean isConnected() {
        return clientHandle.isConnected;
    }

    public void run() {
        connectToServer(ip, port);
        Boolean q = true;
        while (q)
            try {
                System.out.println(
                        "What would you like do to: \n1. Add new worker\n2. Remove worker \n3. Change worker position\n4. Quit");
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                int function = Integer.parseInt(reader.readLine());

                switch (function) {
                    case 1: {
                        System.out.println("Name: ");
                        String name = reader.readLine();
                        System.out.println("Surname:");
                        String surname = reader.readLine();
                        System.out.println("Position:\n1. Worker\n2. Manager\n3. Owner");
                        int position = Integer.parseInt(reader.readLine());
                        System.out.println("Payout:");
                        int payout = Integer.parseInt(reader.readLine());
                        Employee tmp = new Employee(name, surname, payout, position);
                        try {
                            addEmployeeToDatabase(tmp);
                            receiveMessage();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                    /*
                     * case 2: { System.out.println("Name: "); String name = reader.readLine();
                     * System.out.println("Surname:"); String surname = reader.readLine(); try {
                     * removeWorkerFromDatabase(name, surname); } catch (TwoSameWorkersException e)
                     * { System.out.println("Employee login: "); String login = reader.readLine();
                     * removeWorkerFromDatabaseByLogin(login); } break; } case 3: {
                     * System.out.println("Name: "); String name = reader.readLine();
                     * System.out.println("Surname:"); String surname = reader.readLine();
                     * System.out.println("New position:\n1. Worker\n2. Manager\n3. Owner"); int
                     * position = Integer.parseInt(reader.readLine()); try {
                     * changePositionOfWorker(name, surname, position); } catch
                     * (TwoSameWorkersException e) { System.out.println("Employee login: "); String
                     * login = reader.readLine(); changePositionOfWorkerByLogin(login, position); }
                     * break; }
                     */
                    case 4: {
                        q = false;
                        break;
                    }
                    default: {
                        System.out.println("Wrong number. Try again..");
                        break;
                    }
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
    }

    public void addEmployeeToDatabase(Employee employee) {
        String message = "000";
        message += "/" + employee.getName() + "/" + employee.getSurname() + "/" + employee.getLogin() + "/"
                + employee.getPassword() + "/" + employee.getPayout() + "/" + employee.getPosition();
        clientHandle.printWriter.println(message);
        clientHandle.printWriter.flush();
    }

    private void receiveMessage() throws Exception{
        try {
            String message = clientHandle.bufferedReader.readLine();
            if(message == "false") throw new Exception();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
