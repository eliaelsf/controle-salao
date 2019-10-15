package com.fabiorHair.controlesalao.geral;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.MappedSuperclass;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class Item implements Serializable {
	
	private static final long serialVersionUID = -6189440867472194101L;

	public abstract Long getId();
	
	@Override
	public String toString() {
		return this.getClass().getName()+"[id=" + getId() + "]";
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getId()).toHashCode();
	}
        
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}

		if (obj instanceof Item) {
			final Item other = (Item) obj;
			return new EqualsBuilder().append(getId(), other.getId()).isEquals();
		} else {
			return false;
		}
	}
}
