package com.example.demo.idgen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.idgen.domain.IdgenVO;

@Repository("idgenRepository")
public interface IdgenRepository extends JpaRepository<IdgenVO, String>{
	public IdgenVO findByTableName(String tableName);
}