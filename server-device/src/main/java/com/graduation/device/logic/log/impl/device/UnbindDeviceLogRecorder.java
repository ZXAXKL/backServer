package com.graduation.device.logic.log.impl.device;

import com.graduation.device.logic.log.OperationRecorder;
import com.graduation.device.table.Device;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UnbindDeviceLogRecorder extends OperationRecorder {

    @Override
    public String type() {
        return "device.unbind";
    }

    @Override
    public String execute(String name, Map<String, Object> properties) {
        Device device = (Device) properties.get("device");
        return String.format("%s 解绑了设备 %s", name, device.getSerialNumber());
    }

}
