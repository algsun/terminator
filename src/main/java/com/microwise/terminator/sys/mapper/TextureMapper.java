package com.microwise.terminator.sys.mapper;

import com.microwise.terminator.common.persistence.TerminatorMapper;
import com.microwise.terminator.sys.entity.Texture;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TextureMapper extends TerminatorMapper<Texture> {
}