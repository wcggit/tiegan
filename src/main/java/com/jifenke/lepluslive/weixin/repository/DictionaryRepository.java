package com.jifenke.lepluslive.weixin.repository;

import com.jifenke.lepluslive.weixin.domain.entities.Dictionary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by xf on 2016/9/27.
 */
public interface DictionaryRepository extends JpaRepository<Dictionary, Long> {

  List<Dictionary> findByName(String name);

}
