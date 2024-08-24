package com.sergeineretin.weatherviewer.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "locations")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(name = "latitude", nullable = false, precision = 10, scale = 4)
    private BigDecimal latitude;
    @Column(name = "longitude", nullable = false, precision = 10, scale = 4)
    private BigDecimal longitude;
}
