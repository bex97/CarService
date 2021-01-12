import java.util.List;

public class Repair {
    Car car;
    ClientDatabase owner;
    Status status;
    Comment comment;
    PaymentStatus paymentStatus;
    PaymentType paymentType;
    Discount discount;
    Employee employee;
    ServiceType servicetype;
    List<CarPart> carPartList;
    


    enum Status{
        IN_PROGRESS,
        FINISHED
    }

    enum PaymentStatus{
        NOT_PAID,
        PAID
    }

    enum PaymentType{
        CARD,
        CASH
    }

    enum Discount{
        FIVE_PERCENT,
        TEN_PERCENT,
        FIFTEEN_PERCENT
    }

    enum ServiceType{
        WHEEL_CHANGE,
        AC_CHECK,
        PART_CHANGE,

    }

}
