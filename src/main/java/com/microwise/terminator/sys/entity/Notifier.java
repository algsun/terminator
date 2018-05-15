package com.microwise.terminator.sys.entity;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * Created by bai.weixing on 2017/12/4.
 *
 * 报警通知人
 */
@Component
@Data
public class Notifier {
    /**
     * 通知人唯一标识
     */
    private String id;
    /**
     * 通知人姓名
     */
    private String name;
    /**
     * 邮箱地址
     */
    private String email;
    /**
     * 电话
     */
    private String mobile;
}
