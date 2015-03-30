/*
 * Copyright (C) 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.netcoding.niftybukkit.util.gson.internal.bind;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;

import net.netcoding.niftybukkit.util.gson.Gson;
import net.netcoding.niftybukkit.util.gson.TypeAdapter;
import net.netcoding.niftybukkit.util.gson.TypeAdapterFactory;
import net.netcoding.niftybukkit.util.gson.internal.$Gson$Types;
import net.netcoding.niftybukkit.util.gson.internal.ConstructorConstructor;
import net.netcoding.niftybukkit.util.gson.internal.ObjectConstructor;
import net.netcoding.niftybukkit.util.gson.reflect.TypeToken;
import net.netcoding.niftybukkit.util.gson.stream.JsonReader;
import net.netcoding.niftybukkit.util.gson.stream.JsonToken;
import net.netcoding.niftybukkit.util.gson.stream.JsonWriter;

/**
 * Adapt a homogeneous collection of objects.
 */
public final class CollectionTypeAdapterFactory implements TypeAdapterFactory {
	private static final class Adapter<E> extends TypeAdapter<Collection<E>> {
		private final ObjectConstructor<? extends Collection<E>> constructor;
		private final TypeAdapter<E> elementTypeAdapter;

		public Adapter(Gson context, Type elementType,
				TypeAdapter<E> elementTypeAdapter,
				ObjectConstructor<? extends Collection<E>> constructor) {
			this.elementTypeAdapter = new TypeAdapterRuntimeTypeWrapper<E>(
					context, elementTypeAdapter, elementType);
			this.constructor = constructor;
		}

		@Override
		public Collection<E> read(JsonReader in) throws IOException {
			if (in.peek() == JsonToken.NULL) {
				in.nextNull();
				return null;
			}

			Collection<E> collection = this.constructor.construct();
			in.beginArray();
			while (in.hasNext()) {
				E instance = this.elementTypeAdapter.read(in);
				collection.add(instance);
			}
			in.endArray();
			return collection;
		}

		@Override
		public void write(JsonWriter out, Collection<E> collection)
				throws IOException {
			if (collection == null) {
				out.nullValue();
				return;
			}

			out.beginArray();
			for (E element : collection)
				this.elementTypeAdapter.write(out, element);
			out.endArray();
		}
	}

	private final ConstructorConstructor constructorConstructor;

	public CollectionTypeAdapterFactory(
			ConstructorConstructor constructorConstructor) {
		this.constructorConstructor = constructorConstructor;
	}

	@Override
	public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
		Type type = typeToken.getType();

		Class<? super T> rawType = typeToken.getRawType();
		if (!Collection.class.isAssignableFrom(rawType))
			return null;

		Type elementType = $Gson$Types.getCollectionElementType(type, rawType);
		TypeAdapter<?> elementTypeAdapter = gson.getAdapter(TypeToken
				.get(elementType));
		ObjectConstructor<T> constructor = this.constructorConstructor
				.get(typeToken);

		@SuppressWarnings({ "unchecked", "rawtypes" })
		// create() doesn't define a type parameter
		TypeAdapter<T> result = new Adapter(gson, elementType,
				elementTypeAdapter, constructor);
		return result;
	}
}