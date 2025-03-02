package com.github.rafaelfernandes.delivery.application.domain.model;

import com.github.rafaelfernandes.delivery.common.enums.DeliveryStatus;
import com.github.rafaelfernandes.delivery.common.enums.NotificationStatus;
import com.github.rafaelfernandes.delivery.common.validation.ValueOfEnumDeliveryStatus;
import com.github.rafaelfernandes.delivery.common.validation.ValueOfEnumNotificationStatus;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.github.rafaelfernandes.delivery.common.validation.Validation.validate;

@Getter
public class Delivery {

    private final DeliveryId id;

    private Resident resident;

    private Employee employee;

    @NotEmpty(message = "Nome do destinatário deve ser preenchido")
    @Length(min = 3, max = 100, message = "O campo deve ter no minimo {min} e no maximo {max} caracteres")
    private final String destinationName;

    @NotEmpty(message = "Descrição do pacote deve ser preenchida")
    @Length(min = 3, max = 100, message = "O campo deve ter no minimo {min} e no maximo {max} caracteres")
    private final String packageDescription;

    @NotEmpty(message = "Tipo de usuário deve ser preenchido")
    @ValueOfEnumDeliveryStatus(enumClass = DeliveryStatus.class, message = "Tipo de usuário inválido")
    private String deliveryStatus;

    @NotEmpty(message = "Status de notificação deve ser preenchido")
    @ValueOfEnumNotificationStatus(enumClass = NotificationStatus.class, message = "Status de notificação inválido")
    private String notificationStatus;

    private final LocalDateTime enterDate;

    private LocalDateTime exitDate;

    private String receiverName;

    public record DeliveryId(
            @NotEmpty(message = "O campo deve ser do tipo UUID")
            @org.hibernate.validator.constraints.UUID(message = "O campo deve ser do tipo UUID")
            String id
    ) {
        public DeliveryId(String id) {
            this.id = id;
            validate(this);
        }
    }

    public Delivery(Resident resident, Employee employee, String destinationName, String packageDescription) {
        this.resident = resident;
        this.employee = employee;
        this.packageDescription = packageDescription;
        this.destinationName = destinationName;
        this.deliveryStatus = DeliveryStatus.TO_DELIVER.name();
        this.enterDate = LocalDateTime.now();
        this.notificationStatus = NotificationStatus.TO_SEND.name();
        this.id = new DeliveryId(java.util.UUID.randomUUID().toString());
        validate(this);
    }




    private Delivery(String id, Resident resident, Employee employee, String destinationName, String packageDescription, String deliveryStatus, String notificationStatus, LocalDateTime enterDate, LocalDateTime exitDate, String receiverName) {
        this.id = new DeliveryId(id);
        this.resident = resident;
        this.employee = employee;
        this.packageDescription = packageDescription;
        this.deliveryStatus = deliveryStatus;
        this.notificationStatus = notificationStatus;
        this.enterDate = enterDate;
        this.exitDate = exitDate;
        this.receiverName = receiverName;
        this.destinationName = destinationName;
        validate(this);
    }

    public static Delivery of(String id, Resident resident, Employee employee, String destinationName, String packageDescription, String deliveryStatus, String notificationStatus, LocalDateTime enterDate, LocalDateTime exitDate, String receiverName) {
        return new Delivery(id, resident, employee, destinationName, packageDescription, deliveryStatus, notificationStatus, enterDate, exitDate, receiverName);
    }



}
