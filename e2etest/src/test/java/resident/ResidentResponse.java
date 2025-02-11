package resident;

public record ResidentResponse(

        String id,

        String name,

        String document,

        String cellphone,

        Integer apartment
) {}
