package com.example.demo.idgen.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("serial")
@Entity
@Table(name = "seq_mng")
@Data
public class IdgenVO implements Serializable{
	@Id
	@Column(name = "table_name")
	private String tableName;
	@Column(name = "next_id")
	private int nextId;
}