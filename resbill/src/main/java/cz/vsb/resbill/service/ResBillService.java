package cz.vsb.resbill.service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cz.vsb.resbill.exception.ResBillException;

/**
 * Annotation for marking application services ensuring required configuration
 * 
 * @author HAL191
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Service
@Transactional(rollbackFor = ResBillException.class)
public @interface ResBillService {

}
