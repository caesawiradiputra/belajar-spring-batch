/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.co.telkomsigma.belajarspringbatch.listener;

import id.co.telkomsigma.belajarspringbatch.domain.Peserta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.annotation.OnSkipInProcess;
import org.springframework.batch.core.annotation.OnSkipInRead;
import org.springframework.batch.core.annotation.OnSkipInWrite;
import org.springframework.stereotype.Component;

/**
 *
 * @author Caesa
 */

@Component
public class CustomSkipListener {
    Logger logger = LoggerFactory.getLogger(CustomSkipListener.class);
    
    @OnSkipInRead
    public void onSkipInRead(Throwable T) {
        logger.error("LISTENER SAAT SKIP IN READ : {}", T.getMessage());
    }
    
    @OnSkipInProcess
    public void onSkipInProcess(Peserta p, Throwable t) {
        logger.error("LISTENER SAAT SKIP IN PROCESS : {}", p);
    }
    
    @OnSkipInWrite
    public void onSkipInWrite(Peserta p, Throwable t) {
        logger.error("LISTENER SAAT SKIP IN WRITE : {}", p);
    }
}
