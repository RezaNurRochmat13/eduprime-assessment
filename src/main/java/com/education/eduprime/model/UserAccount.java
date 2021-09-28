package com.education.eduprime.model;

import com.education.eduprime.utils.AuditModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name = "user_accounts")
@Getter
@Setter
public class UserAccount extends AuditModel {
    @Id
    private Long id;

    @Column(name = "balances")
    private Integer balances;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;
}
