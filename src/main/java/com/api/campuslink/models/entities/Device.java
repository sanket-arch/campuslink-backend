package com.api.campuslink.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Device")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(DeviceId.class)
public class Device {
    @Id
    @Column(name = "BANK_CODE")
    private String bankCode;

    @Id
    @Column(name = "DEVICE_CODE")
    private String deviceCode;

    @Column(name = "NAME")
    private String name;

    @Column(name = "INSTITUTE_CODE")
    private String instituteCode;

    @ManyToOne
    @JoinColumns(
            {@JoinColumn(name = "BANK_CODE", referencedColumnName = "BANK_CODE", updatable = false, insertable = false,foreignKey = @ForeignKey(name = "FK_INSTITUTION_INSTITUTE_CODE")),
                    @JoinColumn(name = "INSTITUTE_CODE", referencedColumnName = "INSTITUTE_CODE", updatable = false, insertable = false,foreignKey = @ForeignKey(name = "FK_INSTITUTION_INSTITUTE_CODE"))
            })
    private Institution institution;
}
