/*
 * Copyright (C) 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.netcoding.niftybukkit.util.gson.internal;

import java.io.EOFException;
import java.io.IOException;
import java.io.Writer;

import net.netcoding.niftybukkit.util.gson.JsonElement;
import net.netcoding.niftybukkit.util.gson.JsonIOException;
import net.netcoding.niftybukkit.util.gson.JsonNull;
import net.netcoding.niftybukkit.util.gson.JsonParseException;
import net.netcoding.niftybukkit.util.gson.JsonSyntaxException;
import net.netcoding.niftybukkit.util.gson.internal.bind.TypeAdapters;
import net.netcoding.niftybukkit.util.gson.stream.JsonReader;
import net.netcoding.niftybukkit.util.gson.stream.JsonWriter;
import net.netcoding.niftybukkit.util.gson.stream.MalformedJsonException;

/**
 * Reads and writes GSON parse trees over streams.
 */
public final class Streams {
	/**
	 * Adapts an {@link Appendable} so it can be passed anywhere a
	 * {@link Writer} is used.
	 */
	private static final class AppendableWriter extends Writer {
		/**
		 * A mutable char sequence pointing at a single char[].
		 */
		static class CurrentWrite implements CharSequence {
			char[] chars;

			@Override
			public char charAt(int i) {
				return this.chars[i];
			}

			@Override
			public int length() {
				return this.chars.length;
			}

			@Override
			public CharSequence subSequence(int start, int end) {
				return new String(this.chars, start, end - start);
			}
		}

		private final Appendable appendable;

		private final CurrentWrite currentWrite = new CurrentWrite();

		private AppendableWriter(Appendable appendable) {
			this.appendable = appendable;
		}

		@Override
		public void close() {
		}

		@Override
		public void flush() {
		}

		@Override
		public void write(char[] chars, int offset, int length)
				throws IOException {
			this.currentWrite.chars = chars;
			this.appendable.append(this.currentWrite, offset, offset + length);
		}

		@Override
		public void write(int i) throws IOException {
			this.appendable.append((char) i);
		}
	}

	/**
	 * Takes a reader in any state and returns the next value as a JsonElement.
	 */
	public static JsonElement parse(JsonReader reader)
			throws JsonParseException {
		boolean isEmpty = true;
		try {
			reader.peek();
			isEmpty = false;
			return TypeAdapters.JSON_ELEMENT.read(reader);
		} catch (EOFException e) {
			/*
			 * For compatibility with JSON 1.5 and earlier, we return a JsonNull
			 * for empty documents instead of throwing.
			 */
			if (isEmpty)
				return JsonNull.INSTANCE;
			// The stream ended prematurely so it is likely a syntax error.
			throw new JsonSyntaxException(e);
		} catch (MalformedJsonException e) {
			throw new JsonSyntaxException(e);
		} catch (IOException e) {
			throw new JsonIOException(e);
		} catch (NumberFormatException e) {
			throw new JsonSyntaxException(e);
		}
	}

	/**
	 * Writes the JSON element to the writer, recursively.
	 */
	public static void write(JsonElement element, JsonWriter writer)
			throws IOException {
		TypeAdapters.JSON_ELEMENT.write(writer, element);
	}

	public static Writer writerForAppendable(Appendable appendable) {
		return appendable instanceof Writer ? (Writer) appendable
				: new AppendableWriter(appendable);
	}

}