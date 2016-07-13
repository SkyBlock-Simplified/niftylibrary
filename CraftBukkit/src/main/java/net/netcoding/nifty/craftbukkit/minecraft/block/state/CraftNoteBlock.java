package net.netcoding.nifty.craftbukkit.minecraft.block.state;

import net.netcoding.nifty.common.minecraft.block.state.NoteBlock;
import net.netcoding.nifty.common.minecraft.sound.Instrument;
import net.netcoding.nifty.common.minecraft.sound.Note;

public final class CraftNoteBlock extends CraftBlockState implements NoteBlock {

	public CraftNoteBlock(org.bukkit.block.NoteBlock noteBlock) {
		super(noteBlock);
	}

	private org.bukkit.Note createNote(Note note) {
		return new org.bukkit.Note(note.getOctave(), org.bukkit.Note.Tone.valueOf(note.getTone().name()), note.isSharped());
	}

	@Override
	public org.bukkit.block.NoteBlock getHandle() {
		return (org.bukkit.block.NoteBlock)super.getHandle();
	}

	@Override
	public Note getNote() {
		org.bukkit.Note bukkitNote = this.getHandle().getNote();
		return new Note(bukkitNote.getOctave(), Note.Tone.valueOf(bukkitNote.getTone().name()), bukkitNote.isSharped());
	}

	@Override
	public boolean play() {
		return this.getHandle().play();
	}

	@Override
	public boolean play(Instrument instrument, Note note) {
		return this.getHandle().play(org.bukkit.Instrument.valueOf(instrument.name()), this.createNote(note));
	}

	@Override
	public void setNote(Note note) {
		this.getHandle().setNote(this.createNote(note));
	}

}