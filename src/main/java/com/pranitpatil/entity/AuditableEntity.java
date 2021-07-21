package com.pranitpatil.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@MappedSuperclass
public class AuditableEntity {

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(updatable = false, name = "CREATED_AT")
    @CreationTimestamp
    private Date createdAt;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(updatable = true, name = "LAST_MODIFIED_AT")
    @UpdateTimestamp
    private Date lastModifiedAt;

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getLastModifiedAt() {
        return lastModifiedAt;
    }

    public void setLastModifiedAt(Date lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }

}
