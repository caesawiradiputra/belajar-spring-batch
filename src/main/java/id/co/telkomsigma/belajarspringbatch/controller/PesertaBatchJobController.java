/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.co.telkomsigma.belajarspringbatch.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Caesa
 */
@RestController
public class PesertaBatchJobController {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier("importDataPesertaFromCsvJob")
    private Job importDataPesertaFromCsvJob;
    
    @Autowired
    @Qualifier("exportPesertaJob")
    private Job exportPesertaJob;

    Logger logger = LoggerFactory.getLogger(PesertaBatchJobController.class);

    @GetMapping("/runPesertaBatchJob")
    public String runPesertaBatchJob() {
        logger.info("runPesertaBatchJob");
        try {
            JobParameters parameter = new JobParametersBuilder()
                    .addString("JobId", "2")
                    .toJobParameters();
            jobLauncher.run(importDataPesertaFromCsvJob, parameter);
        } catch (Exception e) {
            logger.error("Error Launch importDataPesertaFromCsvJob : ", e.getMessage(), e);
            return "Error Launch importDataPesertaFromCsvJob : " + e.getMessage();
        }
        return "Job Done";
    }

    @GetMapping("/runPesertaBatchJob/{id}")
    public String runPesertaBatchJob(@PathVariable("id") String s) {
        logger.info("runPesertaBatchJob");
        try {
            JobParameters parameter = new JobParametersBuilder()
                    .addString("JobId", s)
                    .toJobParameters();
            jobLauncher.run(importDataPesertaFromCsvJob, parameter);
        } catch (Exception e) {
            logger.error("Error Launch importDataPesertaFromCsvJob : ", e.getMessage(), e);
            return "Error Launch importDataPesertaFromCsvJob : " + e.getMessage();
        }
        return "Job Done";
    }

    @GetMapping("/runExportPesertaJob/{id}")
    public String runExportPesertaJob(@PathVariable("id") String s) {
        logger.info("runExportPesertaJob");
        try {
            JobParameters parameter = new JobParametersBuilder()
                    .addString("JobId", s)
                    .toJobParameters();
            jobLauncher.run(exportPesertaJob, parameter);
        } catch (Exception e) {
            logger.error("Error Launch exportPesertaJob : ", e.getMessage(), e);
            return "Error Launch exportPesertaJob : " + e.getMessage();
        }
        return "exportPesertaJob Done";
    }
}
