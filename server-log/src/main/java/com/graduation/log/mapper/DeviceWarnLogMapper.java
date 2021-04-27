package com.graduation.log.mapper;

import com.graduation.log.table.DeviceWarnLog;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface DeviceWarnLogMapper extends Mapper<DeviceWarnLog> {
}