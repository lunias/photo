package com.ethanaa.photo.security.entity.base;


import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.springframework.hateoas.EntityModel;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@MappedSuperclass
public abstract class SequentialEntity extends EntityModel<Long> implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    public SequentialEntity() {
        //
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SequentialEntity that = (SequentialEntity) o;
        return Objects.equal(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .toString();
    }
}
