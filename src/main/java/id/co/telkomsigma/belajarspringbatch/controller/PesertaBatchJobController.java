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
    private Job importDataPesertaFromCsvFile;

    Logger logger = LoggerFactory.getLogger(PesertaBatchJobController.class);

    @GetMapping("/runPesertaBatchJob")
    public String runPesertaBatchJob() {
        logger.info("runPesertaBatchJob");
        try {
            JobParameters parameter = new JobParametersBuilder()
                    .addString("JobId", "2")
                    .toJobParameters();
            jobLauncher.run(importDataPesertaFromCsvFile, parameter);
        } catch (Exception e) {
            logger.error("Error Launch importDataPesertaFromCsvFile : ", e.getMessage(), e);
            return "Error Launch importDataPesertaFromCsvFile : " + e.getMessage();
        }
        return "Job Done";
    }

    @GetMapping("/runPesertaBatchJob/{id}")
    public String runPesertaBatchJob(@PathVariable("id") String s) {
        logger.info("runPesertaBatchJob");
        try {
            JobParameters parameter = new JobParametersBuilder()
                    .addString("JobId", s.toString())
                    .toJobParameters();
            jobLauncher.run(importDataPesertaFromCsvFile, parameter);
        } catch (Exception e) {
            logger.error("Error Launch importDataPesertaFromCsvFile : ", e.getMessage(), e);
            return "Error Launch importDataPesertaFromCsvFile : " + e.getMessage();
        }
        return "Job Done";
    }
}
