package delivery;

public record DeliveryRequest(
        Integer apartment,
        String employeeCellphone,
        String nameDestination,
        String packageDescription
) {
}
