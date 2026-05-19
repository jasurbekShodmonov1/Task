package com.example.task.entity.base;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
@MappedSuperclass
public abstract class BaseDomain<T extends Serializable> implements Serializable {
  @Id @GeneratedValue private T id;
}
