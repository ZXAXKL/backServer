package com.graduation.log.mapper;

import com.graduation.log.table.OperationLog;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface OperationLogMapper extends Mapper<OperationLog> {
}
