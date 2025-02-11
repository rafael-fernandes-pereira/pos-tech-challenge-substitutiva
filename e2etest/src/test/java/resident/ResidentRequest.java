package resident;

public record ResidentRequest(
        String name,

        String document,

        String cellphone,

        Integer apartment

) {
}
