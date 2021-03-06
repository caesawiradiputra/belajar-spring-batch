/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.co.telkomsigma.belajarspringbatch.listener;

import id.co.telkomsigma.belajarspringbatch.domain.Peserta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.annotation.AfterRead;
import org.springframework.batch.core.annotation.BeforeRead;
import org.springframework.batch.core.annotation.OnReadError;
import org.springframework.stereotype.Component;

/**
 *
 * @author Caesa
 */
@Component
public class ItemReaderListener {
    Logger logger = LoggerFactory.getLogger(ItemReaderListener.class);
    
    @BeforeRead
    public void beforeRead(){
        logger.info("Interceptor sebelum baca file");
    }
    
    @AfterRead
    public void afterRead(Peserta p){
        logger.info("Interceptor setelah baca file : {}", p );
        
    }
    
    @OnReadError
    public void onReadError(Exception ex){
        logger.error("Interceptor error saat baca file : {}", ex.getMessage());
        
    }
    
}
