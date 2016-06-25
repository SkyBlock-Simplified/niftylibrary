package net.netcoding.nifty.common._new_.minecraft.block;

import net.netcoding.nifty.common._new_.minecraft.sound.Instrument;
import net.netcoding.nifty.common._new_.minecraft.sound.Note;

public interface NoteBlock extends BlockState {

	/**
	 * Gets the note.
	 *
	 * @return The note.
	 */
	Note getNote();

	/**
	 * Gets the note.
	 *
	 * @return The note ID.
	 */
	default byte getRawNote() {
		return this.getNote().getId();
	}

	/**
	 * Attempts to play the note at block.
	 * <p>
	 * If the block is no longer a note block, this will return false
	 *
	 * @return True if successful, otherwise false
	 */
	boolean play();

	/**
	 * Plays an arbitrary note with an arbitrary instrument.
	 *
	 * @param instrument Instrument ID
	 * @param note Note ID
	 * @return true if successful, otherwise false
	 */
	default boolean play(byte instrument, byte note) {
		return this.play(Instrument.getByType(instrument), new Note(note));
	}

	/**
	 * Plays an arbitrary note with an arbitrary instrument.
	 *
	 * @param instrument The instrument
	 * @param note The note
	 * @return true if successful, otherwise false
	 * @see Instrument Note
	 */
	boolean play(Instrument instrument, Note note);

	/**
	 * Set the note.
	 *
	 * @param note The note.
	 */
	void setNote(Note note);

	/**
	 * Set the note.
	 *
	 * @param note The note ID.
	 */
	default void setRawNote(byte note) {
		this.setNote(new Note(note));
	}

}