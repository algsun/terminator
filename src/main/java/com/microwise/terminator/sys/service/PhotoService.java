package com.microwise.terminator.sys.service;

import com.microwise.terminator.common.service.CrudService;
import com.microwise.terminator.sys.entity.Photo;
import com.microwise.terminator.sys.mapper.PhotoMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 文物照片service
 *
 * @author bai.weixing
 * @since 2017/9/22.
 */
@Service
@Transactional
public class PhotoService extends CrudService<PhotoMapper, Photo> {
}
