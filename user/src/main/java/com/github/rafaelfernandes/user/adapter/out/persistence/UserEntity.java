package com.github.rafaelfernandes.user.adapter.out.persistence;

import com.github.rafaelfernandes.common.enums.UserType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_user")
public class UserEntity {

    @Id
    @GenericGenerator(name = "UUIDGenerator", strategy = "uuid2")
    @GeneratedValue(generator = "UUIDGenerator")
    private UUID id;

    private UUID residentId;

    private String password;

    private UserType type;

}
