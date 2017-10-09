/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.co.telkomsigma.belajarspringbatch.writter;

import id.co.telkomsigma.belajarspringbatch.dao.PesertaDao;
import id.co.telkomsigma.belajarspringbatch.domain.Peserta;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Caesa
 */
@Component
public class PesertaItemWritter implements ItemWriter<Peserta>{
    Logger logger = LoggerFactory.getLogger(PesertaItemWritter.class);
    
    @Autowired
    PesertaDao pesertaDao;
   
    @Override
    public void write(List<? extends Peserta> list) throws Exception {
        for (Peserta peserta : list) {
            logger.info("PESERTA YANG AKAN DI SAVE : {}",peserta.getNama());
            pesertaDao.save(peserta);
        }
    }
    
    
}
