package com.abhirambsn.importexportservice.service;

import com.abhirambsn.importexportservice.entity.DataQuery;
import com.abhirambsn.importexportservice.entity.FileType;
import com.abhirambsn.importexportservice.entity.Job;
import com.abhirambsn.importexportservice.entity.JobStatus;
import com.abhirambsn.importexportservice.listener.JobExecutionException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import lombok.extern.slf4j.Slf4j;
import org.dhatim.fastexcel.Workbook;
import org.dhatim.fastexcel.Worksheet;
import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class JobProcessor {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${api.baseUrl}")
    private String baseUrl;

    public JobProcessor(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public Job process(Job job) throws JobExecutionException {
        try {
            validateJob(job);
            switch (job.getRequest().getJobType()) {
                case EXPORT -> {
                    String fileUrl = handleExport(job);
                    job.setStatus(JobStatus.COMPLETED);
                    job.setDownloadUrl(fileUrl);
                }
                case IMPORT -> {
                    handleImport(job);
                    job.setStatus(JobStatus.COMPLETED);
                }
                default -> {
                    throw new IllegalArgumentException("Invalid job type: " + job.getRequest().getJobType());
                }
            }
            return job;
        } catch (Exception e) {
            throw new JobExecutionException(String.format("Job execution failed with error: %s", e), job);
        }
    }

    private void validateJob(Job job) {
        if (job.getRequest() == null || job.getRequest().getJobType() == null) {
            throw new IllegalArgumentException("Invalid job request: missing required fields.");
        }
        log.info("Validated job: {}", job.getId());
    }


    private String handleExport(Job job) throws URISyntaxException, FileNotFoundException {
        log.info("Processing Export Job: {}", job.getId());
        FileType type = job.getRequest().getFileType();
        List<Map<String, Object>> data = fetchExpenseData(job);
        String file = null;

        switch (type) {
            case CSV -> {
                file = exportAsCSV(job, data);
            }
            case EXCEL -> {
                file = exportAsExcel(job, data);
            }
            case JSON -> {
                file = exportAsJson(job, data);
            }
            default -> {
                throw new IllegalArgumentException("Invalid file type: " + type);
            }
        }
        return file;
    }

    private List<Map<String, Object>> fetchExpenseData(Job job) throws URISyntaxException {
        // Fetch data from ExpenseService
        URI expenseServiceUrl = new URI(baseUrl + job.getRequest().getEntity() + "/");
        DataQuery query = job.getRequest().getQuery();
        // Build Query Params from the same

        Map<String, Object> queryParams = new HashMap<>();
        if (query.getFilter() != null) {
            queryParams.put("$filter", query.getFilter());
        }
        if (query.getOrderBy() != null) {
            queryParams.put("$orderBy", query.getOrderBy());
        }
        queryParams.put("$top", query.getTop());
        queryParams.put("$skip", query.getSkip());

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-TenantID", job.getTenantId());

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromUri(expenseServiceUrl);
            queryParams.forEach(builder::queryParam);
            log.info("Fetching data from ExpenseService: {}", builder.toUriString());
            ResponseEntity<Map> response = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    requestEntity,
                    Map.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
                Object data = responseBody.get("data");
                if (data instanceof List) {
                    return (List<Map<String, Object>>) data;
                } else {
                    throw new RuntimeException("Invalid response from ExpenseService: " + data);
                }
            } else {
                throw new RuntimeException("Failed to fetch data from ExpenseService: " + response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Failed to fetch data from ExpenseService: ", e);
            throw new RuntimeException(e);
        }
    }

    private void handleImport(Job job) throws URISyntaxException {
        log.info("Processing Import Job: {}", job.getId());
        FileType type = job.getRequest().getFileType();

        List<Map<String, Object>> data;
        switch (type) {
            case CSV -> {
                data = importFromCsv(job);
            }
            case EXCEL -> {
                data = importFromExcel(job);
            }
            case JSON -> {
                data = importFromJson(job);
            }
            default -> {
                throw new IllegalArgumentException("Invalid file type: " + type);
            }
        }
        log.info("Imported data: {}", data);
        sendDataToService(job.getRequest().getEntity(), data, job.getTenantId());
    }

    public void sendDataToService(String entity, List<Map<String, Object>> data, String tenantId) throws URISyntaxException {
        String url = baseUrl + entity + "/$bulk";
        URI uri = new URI(url);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-TenantID", tenantId);
        headers.set("Content-Type", "application/json");

        HttpEntity<List<Map<String, Object>>> requestEntity = new HttpEntity<>(data, headers);

        try {
            ResponseEntity<Object> response = restTemplate.exchange(
                    uri.toString(),
                    HttpMethod.POST,
                    requestEntity,
                    Object.class
            );

            if (!response.getStatusCode().is2xxSuccessful()) {
                log.error("Failed to send data to ExpenseService: {}", response.getStatusCode());
                throw new RuntimeException("Failed to send data to ExpenseService: " + response.getStatusCode());
            }

            log.info("Data sent to ExpenseService: {}", response.getBody());
        } catch (Exception e) {
            log.error("Failed to send data to ExpenseService: ", e);
            throw new RuntimeException(e);
        }
    }

    private String exportAsCSV(Job job, List<Map<String, Object>> data) {
        log.info("Exporting as CSV: {}", job.getId());
        String root_path = new FileSystemResource("").getFile().getAbsolutePath();
        String file_path = root_path + "/exported_files/" + job.getId() + ".csv";
        try (FileWriter writer = new FileWriter(file_path)) {
            CSVWriter csvWriter = new CSVWriter(writer);

            String[] headers = data.getFirst().keySet().toArray(new String[0]);
            csvWriter.writeNext(headers);

            for (Map<String, Object> row : data) {
                String[] values = row.values().stream()
                        .map(this::formatCellValue)
                        .toArray(String[]::new);
                csvWriter.writeNext(values);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return file_path;
    }

    private String exportAsExcel(Job job, List<Map<String, Object>> data) {
        String root_path = new FileSystemResource("").getFile().getAbsolutePath();
        String file_path = root_path + "/exported_files/" + job.getId() + ".xlsx";
        log.info("Exporting as Excel: {}", job.getId());

        try (OutputStream os = new FileOutputStream(file_path)) {
            Workbook workbook = new Workbook(os, "Export", "1.0");
            Worksheet sheet = workbook.newWorksheet(job.getRequest().getEntity());

            List<String> headers = new ArrayList<>(data.getFirst().keySet());
            for (int i = 0; i < headers.size(); i++) {
                sheet.value(0, i, headers.get(i));
            }

            for (int rowIdx = 0; rowIdx < data.size(); rowIdx++) {
                Map<String, Object> row = data.get(rowIdx);
                int colIdx = 0;
                for (String header : headers) {
                    Object value = row.get(header);
                    sheet.value(rowIdx + 1, colIdx++, formatCellValue(value));
                }
            }

            workbook.finish();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return file_path;
    }

    private String exportAsJson(Job job, List<Map<String, Object>> data) {
        log.info("Exporting as JSON: {}", job.getId());
        String root_path = new FileSystemResource("").getFile().getAbsolutePath();
        String file_path = root_path + "/exported_files/" + job.getId() + ".json";
        try (FileWriter writer = new FileWriter(file_path)) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(writer, data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return file_path;
    }

    private String formatCellValue(Object value) {
        if (value == null) {
            return "";
        } else if (value instanceof Map) {
            Object id = ((Map<?, ?>) value).get("id");
            return id != null ? id.toString() : "";
        } else {
            return value.toString();
        }
    }

    private List<Map<String, Object>> importFromCsv(Job job) {
        log.info("Importing from CSV: {}", job.getId());
        try {
            log.info("Reading file: {}", job.getRequest().getFileUrl());
            try (FileReader reader = new FileReader(job.getRequest().getFileUrl())) {
                CSVReader csvReader = new CSVReader(reader);

                List<String[]> rows = csvReader.readAll();

                return getJsonFromCsv(rows);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static List<Map<String, Object>> getJsonFromCsv(List<String[]> rows) {
        if (rows.isEmpty()) {
            throw new IllegalArgumentException("No data found in CSV file.");
        }

        String[] headers = rows.getFirst();
        List<Map<String, Object>> data = new ArrayList<>();

        for (int i = 1; i < rows.size(); i++) {
            String[] row = rows.get(i);
            Map<String, Object> record = new HashMap<>();
            for (int j = 0; j < headers.length; j++) {
                record.put(headers[j], row[j]);
            }
            data.add(record);
        }
        return data;
    }

    private List<Map<String, Object>> importFromExcel(Job job) {
        log.info("Importing from Excel: {}", job.getId());
        try {
            log.info("Reading file: {}", job.getRequest().getFileUrl());
            try (
                    InputStream is = new FileInputStream(job.getRequest().getFileUrl());
                    ReadableWorkbook workbook = new ReadableWorkbook(is)) {
                List<Map<String, Object>> data = new ArrayList<>();

                workbook.getSheets().forEach(sheet -> {
                    try {
                        sheet.openStream().forEach(row -> {
                            Map<String, Object> record = new HashMap<>();
                            row.forEach(cell -> {
                                String header = cell.getText();
                                String value = cell.asString();
                                record.put(header, value);
                            });
                            data.add(record);
                        });
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                return data;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<Map<String, Object>> importFromJson(Job job) {
        log.info("Importing from JSON: {}", job.getId());
        try (InputStream is = new FileInputStream(job.getRequest().getFileUrl())) {
            return (List<Map<String, Object>>) objectMapper.readValue(is, List.class);
        } catch (IOException e) {
            throw new RuntimeException("Error reading JSON file", e);
        }
    }


}
