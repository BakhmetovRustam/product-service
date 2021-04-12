package com.brunocesar.product.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Persistable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@ToString
@Table(name = "products")
@Setter(AccessLevel.PACKAGE)
@EqualsAndHashCode(of = "id")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product implements Persistable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false, length = 5)
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 3)
    private Category category;

    @Column(nullable = false, columnDefinition = "timestamp not null default current_timestamp")
    private LocalDateTime lastUpdate;

    @PrePersist
    @PreUpdate
    public void updateLastUpdate() {
        this.lastUpdate = LocalDateTime.now();
    }

    @Override
    @JsonIgnore
    public boolean isNew() {
        return getId() == null;
    }

}
