/*
 *
 */
package com.easynotes.controller;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easynotes.exception.NotesBaseException;
import com.easynotes.model.DemoProperties;
import com.easynotes.model.Error;
import com.easynotes.model.Note;
import com.easynotes.model.ServiceResponse;
import com.easynotes.repository.NoteRepository;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

@RestController
@RequestMapping("/api")
@Slf4j
public class NoteController {

	@Autowired
	DemoProperties c;
	@Autowired
	NoteRepository noteRepository;
	final String reportPath = "c:/Users/naren";
	// Create a new Note
	@PostMapping("/notes")
	public ServiceResponse<Long> createNote(@Valid @RequestBody Note note) {
		final ServiceResponse<Long> response = new ServiceResponse<>();
		final Optional<Note> noteEnt = noteRepository.saveReturnOptional(note);
		noteEnt.ifPresent(noteRetn -> response.setData(noteRetn.getId()));


		return response;
	}

	// Delete a Note
	@DeleteMapping("/notes/{id}")
	public ServiceResponse<Long> deleteNote(
			@PathVariable(value = "id") Long noteId) {
		final ServiceResponse<Long> response = new ServiceResponse<>();

		final Note note = noteRepository.findById(noteId)
				.orElseThrow(() -> new NotesBaseException(
						String.format(c.getnote,
								"no records found related to this note id"),
						HttpStatus.NO_CONTENT.value()));
		noteRepository.delete(note);
		response.setData(note.getId());
		return response;

	}
	@SuppressWarnings("deprecation")
	@GetMapping("/report")
	public String generateReport(String reportType) {
		final List<Note> notes = noteRepository.findAll();
		// Compile the Jasper report from .jrxml to .japser
		try {
			final InputStream employeeReportStream = getClass().getResourceAsStream("/easynotes.jrxml");
			final JasperReport jasperReport = JasperCompileManager
					.compileReport(employeeReportStream);
			// Get your data source
			final JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(notes);
			// Add parameters
			final Map<String, Object> parameters = new HashMap<>();

			parameters.put("createdBy", "Narendra Kumar Kolli");
			// Fill the report
			final JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
					jrBeanCollectionDataSource);
			// print report to file
			final JRXlsxExporter exporter = new JRXlsxExporter();
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(reportPath + "/Emp-Rpt-Database.xlsx"));
			final SimpleXlsxReportConfiguration reportConfig = new SimpleXlsxReportConfiguration();
			reportConfig.setCollapseRowSpan(true);
			reportConfig.setDetectCellType(true);
			reportConfig.setSheetNames(new String[] { "Employee Data" });

			exporter.setConfiguration(reportConfig);
			try {
				exporter.exportReport();
			} catch (final JRException ex) {
				log.error("{}",ex);
			}

			return "Report successfully generated @path= " + reportPath;
		} catch (final Exception e) {
			e.printStackTrace();
			return "Error--> check the console log";
		}

	}

	// Get All Notes
	@GetMapping("/notes")
	public ServiceResponse<List<Note>> getAllNotes() {
		final ServiceResponse<List<Note>> response = new ServiceResponse<>();
		final List<Note> allNotes = noteRepository.findAll();
		if (!CollectionUtils.isEmpty(allNotes)) {
			response.setData(noteRepository.findAll());
		} else {
			response.setCode(-1);
			final Error error = new Error();
			error.setMessage(String.format(c.getallnotes,
					"no records found related to this note id"));
			error.setError(HttpStatus.NO_CONTENT.name());
			response.setError(error);

		}

		return response;
	}

	// Get a Single Note
	@GetMapping("/notes/{id}")
	public ServiceResponse<Note> getNoteById(
			@PathVariable(value = "id") Long noteId)
	{
		final ServiceResponse<Note> response = new ServiceResponse<>();

		final Note note = noteRepository.findById(noteId)
				.orElseThrow(() -> new NotesBaseException(
						String.format(c.getnote,
								"no records found related to this note id"),
						HttpStatus.NO_CONTENT.value()));
		response.setData(note);
		return response;
	}



	// Update a Note
	@PutMapping("/notes/{id}")
	public ServiceResponse<Note> updateNote(
			@PathVariable(value = "id") Long noteId,
			@Valid @RequestBody Note noteDetails) {
		final ServiceResponse<Note> response = new ServiceResponse<>();

		final Note note = noteRepository.findById(noteId)
				.orElseThrow(() -> new NotesBaseException(
						String.format(c.getnote,
								"no records found related to this note id"),
						HttpStatus.NO_CONTENT.value()));

		note.setTitle(noteDetails.getTitle());
		note.setContent(noteDetails.getContent());
		note.setCreatedAt(note.getCreatedAt());
		final Note updatedNote = noteRepository.save(note);
		response.setData(updatedNote);
		return response;
	}
}
