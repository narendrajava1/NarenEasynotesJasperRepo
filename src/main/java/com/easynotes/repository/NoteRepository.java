/*
 *
 */

package com.easynotes.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.easynotes.model.Note;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

	default Optional<Note> saveReturnOptional(Note note) {

		return Optional.of(this.save(note));

	}

}