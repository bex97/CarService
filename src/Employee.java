import java.util.Random;

public class Employee implements Person {
    String name;
    String surname;
    String login;
    String password;
    int payout;
    Position position;

    Employee(String name, String surname, int payout, int position) {
        this.name = name;
        this.surname = surname;
        this.payout = payout;
        setPosition(position);
        generateLoginAndPassword();
    }

    Employee(String name, String surname, String login, String password, int payout, String position) {
        this.name = name;
        this.surname = surname;
        this.login = login;
        this.password = password;
        this.payout = payout;
        this.position = Position.getPosition(position);
    }

    private void generateLoginAndPassword() {
        login = this.name.charAt(0) + this.surname.substring(0, 2) + randomNumbersToLogin();
        password = randomNumbersToPassword();
    }

    private String randomNumbersToLogin() {
        String numbers = "";
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            numbers += Integer.toString(random.nextInt(10));
        }
        return numbers;
    }

    private String randomNumbersToPassword() {
        String numbers = "";
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            numbers += Integer.toString(random.nextInt(10));
        }
        return numbers;
    }

    private void setPosition(int position) {
        switch (position) {
            case 1: {
                this.position = Position.WORKER;
                break;
            }
            case 2: {
                this.position = Position.MANAGER;
                break;
            }
            case 3: {
                this.position = Position.OWNER;
                break;
            }

            default:
                break;
        }
    }

    public String getPosition() {
        return position.toString();
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public int getPayout() {
        return payout;
    }

    public enum Position {
        WORKER {
            public String toString() {
                return "WORKER";
            }
        },
        MANAGER {
            public String toString() {
                return "MANAGER";
            }
        },
        OWNER {
            public String toString() {
                return "OWNER";
            }
        };

        static public Position getPosition(String s)
        {
            switch(s)
            {
                case "WORKER":
                {
                    return Position.WORKER;
                }
                case "MANAGER":
                {
                    return Position.MANAGER;
                }
                case "OWNER":
                {
                    return Position.OWNER;
                }
                default:
                    return null;
            }
        }
    }
}
