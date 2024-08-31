package com.example.demo;

import java.io.FileNotFoundException;
import java.io.File;
import java.io.FileWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@CrossOrigin(origins = "http://localhost:5000")
// @ComponentScan(basePackages = "com.example.demo, " + "com.example")
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	// need to use post cause you want to post data from nodeJS
	@PostMapping("/welcome")
	public ResponseEntity<Resource> first_api(@RequestParam("filePath") String body) throws FileNotFoundException { // need
																													// requestparam
		try {
			System.out.println("Received filePath: " + body);
			String firstFile = "adjlist.in"; // json
			Eligible eligible = new Eligible(firstFile);
			String secondFile = "eligible.in";
			eligible.determineTakenCourses(secondFile);
			eligible.determineEligibeCourses();
			String thirdFile = "eligible.out";
			eligible.print(thirdFile);
			File file = new File(thirdFile);
			InputStreamResource resource = new InputStreamResource(new FileInputStream(file)); // loads file contents
			return ResponseEntity.ok() // controls response sent
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + secondFile)
					.contentType(MediaType.APPLICATION_OCTET_STREAM)
					.contentLength(file.length())
					.body(resource);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(null);
		}

	}

	@PostMapping("/schedule")
	public ResponseEntity<Resource> second_api(@RequestParam("scheduleFilePath") String schedulebody) {
		try {
			String firstFile = "adjlist.in";
			SchedulePlan schedule = new SchedulePlan(firstFile);
			String secondFile = "scheduleplan.in";
			schedule.readPlanner(secondFile);
			schedule.determineOrder();
			schedule.insertionSort();
			String thirdFile = "scheduleplan.out";
			schedule.setOutputFile(thirdFile);
			schedule.printSchedule();
			File file = new File(thirdFile);
			InputStreamResource resource = new InputStreamResource(new FileInputStream(file)); // loads file contents
			return ResponseEntity.ok() // controls response sent
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + secondFile)
					.contentType(MediaType.APPLICATION_OCTET_STREAM)
					.contentLength(file.length())
					.body(resource);			

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(null);
		}
		
	}
	

}
