package com.github.rafaelfernandes.notification.adapter.out.api;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DeliveryDataResponse {
    private String destinationName;
    private String packageDescription;
    private String residentName;
    private String residentCellphone;

}
