/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.co.telkomsigma.belajarspringbatch.config;

import id.co.telkomsigma.belajarspringbatch.domain.Peserta;
import id.co.telkomsigma.belajarspringbatch.listener.ItemReaderListener;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.FieldExtractor;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

/**
 *
 * @author Caesa
 */

@Configuration
public class ExportPesertaJobConfiguration {
    
    Logger logger = LoggerFactory.getLogger(ExportPesertaJobConfiguration.class);
    
    @Autowired
    public JobBuilderFactory jobBuilderFactory;
    
    @Autowired
    public StepBuilderFactory stepBuilderFactory;
    
    @Autowired
    public ItemReaderListener itemReaderListener;
    
    @Autowired
    public DataSource dataSource;
    
    @Bean
    public ItemReader<Peserta> databaseCsvItemReader(){
        
        JdbcPagingItemReader<Peserta> dataReader = new JdbcPagingItemReader<Peserta>();
        
        dataReader.setDataSource(dataSource);
        dataReader.setPageSize(1);
        dataReader.setRowMapper(new BeanPropertyRowMapper<>(Peserta.class));
        dataReader.setQueryProvider(createQueryProvider());
                
        return dataReader;
    }
    
    public PagingQueryProvider createQueryProvider() {
        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
        
        queryProvider.setSelectClause("SELECT * ");
        queryProvider.setFromClause("FROM peserta");
        queryProvider.setSortKeys(sortByNamaAsc());
        
        return queryProvider;
    }
    
    private Map<String, Order> sortByNamaAsc() {
        Map<String, Order> sortConfiguration = new HashMap<>();
        sortConfiguration.put("nama", Order.ASCENDING);
        return sortConfiguration;
    }
    
    @Bean
    public ItemWriter<Peserta> databaseCsvWriter() {
        FlatFileItemWriter<Peserta> csvFileWriter = new FlatFileItemWriter<Peserta>();
        
        String exportFileHeader = "id;nama;alamat;tanggalLahir";
        
        csvFileWriter.setHeaderCallback(new StringHeaderWriter(exportFileHeader));
        csvFileWriter.setResource(new FileSystemResource("/tmp/export-data.csv"));
        csvFileWriter.setLineAggregator(createPesertaLineAggregator());
        
        return csvFileWriter;
    }
    
    private LineAggregator<Peserta> createPesertaLineAggregator(){
        DelimitedLineAggregator<Peserta> delimitedLineAggregator = new DelimitedLineAggregator<Peserta>();
        delimitedLineAggregator.setDelimiter(";");
        delimitedLineAggregator.setFieldExtractor(createPesertaFieldExtractor());
        
        return delimitedLineAggregator;
    }
    
    private FieldExtractor<Peserta> createPesertaFieldExtractor(){
        BeanWrapperFieldExtractor<Peserta> extractor = new BeanWrapperFieldExtractor<Peserta>();
        extractor.setNames(new String[]{"id", "nama", "alamat", "tanggalLahir"});
        return extractor;
    }
    
    @Bean
    public Step databaseToCsvStep(ItemReader<Peserta> itemReader, ItemWriter<Peserta> itemWriter) {
        
        return stepBuilderFactory.get("databaseToCsvStep")
                .<Peserta, Peserta> chunk(1)
                .reader(databaseCsvItemReader())
                .writer(itemWriter)
                .listener(itemReaderListener)
                .build();
    }
    
    @Bean
    public Job exportPesertaJob() {
        return jobBuilderFactory.get("exportPesertaJob")
                .incrementer(new RunIdIncrementer())
                .flow(databaseToCsvStep(databaseCsvItemReader(), databaseCsvWriter()))
                .end()
                .build();
    }

}
