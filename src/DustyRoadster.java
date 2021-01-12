import java.util.List;

public class DustyRoadster {
    List<Cell> mondayCells;
    List<Cell> tuesdayCells;
    List<Cell> wednesdayCells;
    List<Cell> thursdayCells;
    List<Cell> fridayCells;
    List<Cell> saturdayCells;

    public void insertCell(Cell cell)
    {
        switch (cell.dayInWork.getDayOfWeek()) {
            case MONDAY:
            {
                System.out.println("Monday");
                break;
            }
            
            case TUESDAY:
            {
                System.out.println("Tuesday");
                break;
            }
            case WEDNESDAY:
            {
                System.out.println("Wednesday");
                break;
            }
            case THURSDAY:
            {
                System.out.println("Thursday");
                break;
            }
            case FRIDAY:
            {
                System.out.println("Friday");
                break;
            }
            case SATURDAY:
            {
                System.out.println("Saturday");
                break;
            }
                

        
            default:
                break;
        }
    }
}
