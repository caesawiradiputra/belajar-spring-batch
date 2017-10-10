/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.co.telkomsigma.belajarspringbatch.listener;

import id.co.telkomsigma.belajarspringbatch.domain.Peserta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.annotation.AfterWrite;
import org.springframework.batch.core.annotation.BeforeWrite;
import org.springframework.batch.core.annotation.OnWriteError;
import org.springframework.stereotype.Component;

/**
 *
 * @author Caesa
 */

@Component
public class ItemWriterListener {
    Logger logger = LoggerFactory.getLogger(ItemWriterListener.class);
    
    @BeforeWrite
    public void beforeWrite(Peserta p){
        logger.info("Intercept before Write : {}", p);
    }
    
    @AfterWrite
    public void afterWrite(){
        logger.info("Intercept After Writing");
    }
    
    @OnWriteError
    public void onWriteError(){
        logger.info("intercept On Error Writing");
    }
}
