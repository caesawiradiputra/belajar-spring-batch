/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.co.telkomsigma.belajarspringbatch.config;

import id.co.telkomsigma.belajarspringbatch.domain.Peserta;
import id.co.telkomsigma.belajarspringbatch.listener.SkipCheckingListener;
import id.co.telkomsigma.belajarspringbatch.mapper.PesertaMapper;
import id.co.telkomsigma.belajarspringbatch.processor.PesertaItemProcessor;
import id.co.telkomsigma.belajarspringbatch.tasklet.DeletePesertaCsvTasklet;
import id.co.telkomsigma.belajarspringbatch.writter.PesertaItemWritter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

/**
 *
 * @author Caesa
 */
@Configuration
public class PesertaJobConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;
    
    @Autowired
    public PesertaItemWritter itemWritter;
    
    @Autowired
    public PesertaItemProcessor itemProcessor;
    
    @Autowired
    public DeletePesertaCsvTasklet deleteCsvTasklet;
    
    @Autowired
    public SkipCheckingListener skipChecking;

    @Bean
    public FlatFileItemReader<Peserta> reader() {
        FlatFileItemReader<Peserta> reader = new FlatFileItemReader<Peserta>();
        
        reader.setResource(new ClassPathResource("data-peserta.csv"));
        
        reader.setLineMapper(new DefaultLineMapper<Peserta>(){
            {
                setLineTokenizer(new DelimitedLineTokenizer() {
                    {
                        setNames(new String[] {"nama", "alamat", "tanggalLahir"});
                    }
                });
                setFieldSetMapper(new PesertaMapper());
            }
        });
        
//        DefaultLineMapper<Peserta> defaultLineMapper = new DefaultLineMapper<Peserta>();
//        
//        DelimitedLineTokenizer delimitedLineTokenizer - new DelimitedLineTokenizer();
//        delimitedLineTokenizer.setNames(new String[] {"nama", "alamat", "tanggalLahir"});
//        
//        defaultLineMapper.setLineTokenizer(delimitedLineTokenizer);
//        defaultLineMapper.setFieldSetMapper(new PesertaMapper());
//        reader.setLineMapper(defaultLineMapper);

        return reader;
    }
    
    @Bean
    public Job importDataPesertaFromCsvJob() {
        return jobBuilderFactory.get("importDataPesertaFromCsvJob")
                .incrementer(new RunIdIncrementer())
                .flow(step1())
                    .next(step2())
                .end()
                .build();          
                
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<Peserta, Peserta> chunk(1)
                .reader(reader())
                .processor(itemProcessor)
                .writer(itemWritter)
                    .faultTolerant()
                    .skip(FlatFileParseException.class)
                    .skipLimit(1)
                .listener(skipChecking)
                .build();
                
    }
    
    @Bean
    public Step step2(){
        return stepBuilderFactory.get("step2")
                .tasklet(deleteCsvTasklet)
                .build();
    }
}
