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

package net.netcoding.niftybukkit.util.gson;

import net.netcoding.niftybukkit.util.gson.reflect.TypeToken;

/**
 * Creates type adapters for set of related types. Type adapter factories are
 * most useful when several types share similar structure in their JSON form.
 *
 * <h3>Example: Converting enums to lowercase</h3> In this example, we implement
 * a factory that creates type adapters for all enums. The type adapters will
 * write enums in lowercase, despite the fact that they're defined in
 * {@code CONSTANT_CASE} in the corresponding Java model:
 *
 * <pre>
 * {
 * 	&#064;code
 * 	public class LowercaseEnumTypeAdapterFactory implements TypeAdapterFactory {
 * 		public &lt;T&gt; TypeAdapter&lt;T&gt; create(Gson gson, TypeToken&lt;T&gt; type) {
 * 			Class&lt;T&gt; rawType = (Class&lt;T&gt;) type.getRawType();
 * 			if (!rawType.isEnum()) {
 * 				return null;
 * 			}
 *
 * 			final Map&lt;String, T&gt; lowercaseToConstant = new HashMap&lt;String, T&gt;();
 * 			for (T constant : rawType.getEnumConstants()) {
 * 				lowercaseToConstant.put(toLowercase(constant), constant);
 * 			}
 *
 * 			return new TypeAdapter&lt;T&gt;() {
 * 				public void write(JsonWriter out, T value) throws IOException {
 * 					if (value == null) {
 * 						out.nullValue();
 * 					} else {
 * 						out.value(toLowercase(value));
 * 					}
 * 				}
 *
 * 				public T read(JsonReader reader) throws IOException {
 * 					if (reader.peek() == JsonToken.NULL) {
 * 						reader.nextNull();
 * 						return null;
 * 					} else {
 * 						return lowercaseToConstant.get(reader.nextString());
 * 					}
 * 				}
 * 			};
 * 		}
 *
 * 		private String toLowercase(Object o) {
 * 			return o.toString().toLowerCase(Locale.US);
 * 		}
 * 	}
 * }
 * </pre>
 *
 * <p>
 * Type adapter factories select which types they provide type adapters for. If
 * a factory cannot support a given type, it must return null when that type is
 * passed to {@link #create}. Factories should expect {@code create()} to be
 * called on them for many types and should return null for most of those types.
 * In the above example the factory returns null for calls to {@code create()}
 * where {@code type} is not an enum.
 *
 * <p>
 * A factory is typically called once per type, but the returned type adapter
 * may be used many times. It is most efficient to do expensive work like
 * reflection in {@code create()} so that the type adapter's {@code read()} and
 * {@code write()} methods can be very fast. In this example the mapping from
 * lowercase name to enum value is computed eagerly.
 *
 * <p>
 * As with type adapters, factories must be <i>registered</i> with a
 * {@link com.google.gson.GsonBuilder} for them to take effect:
 *
 * <pre>
 * {@code
 *
 *  GsonBuilder builder = new GsonBuilder();
 *  builder.registerTypeAdapterFactory(new LowercaseEnumTypeAdapterFactory());
 *  ...
 *  Gson gson = builder.create();
 * }
 * </pre>
 *
 * If multiple factories support the same type, the factory registered earlier
 * takes precedence.
 *
 * <h3>Example: composing other type adapters</h3>
 * In this example we implement a factory for Guava's {@code Multiset}
 * collection type. The factory can be used to create type adapters for
 * multisets of any element type: the type adapter for {@code Multiset<String>}
 * is different from the type adapter for {@code Multiset<URL>}.
 *
 * <p>
 * The type adapter <i>delegates</i> to another type adapter for the multiset
 * elements. It figures out the element type by reflecting on the multiset's
 * type token. A {@code Gson} is passed in to {@code create} for just this
 * purpose:
 *
 * <pre>
 * {
 * 	&#064;code
 * 	public class MultisetTypeAdapterFactory implements TypeAdapterFactory {
 * 		public &lt;T&gt; TypeAdapter&lt;T&gt; create(Gson gson, TypeToken&lt;T&gt; typeToken) {
 * 			Type type = typeToken.getType();
 * 			if (typeToken.getRawType() != Multiset.class
 * 					|| !(type instanceof ParameterizedType)) {
 * 				return null;
 * 			}
 *
 * 			Type elementType = ((ParameterizedType) type)
 * 					.getActualTypeArguments()[0];
 * 			TypeAdapter&lt;?&gt; elementAdapter = gson.getAdapter(TypeToken
 * 					.get(elementType));
 * 			return (TypeAdapter&lt;T&gt;) newMultisetAdapter(elementAdapter);
 * 		}
 *
 * 		private &lt;E&gt; TypeAdapter&lt;Multiset&lt;E&gt;&gt; newMultisetAdapter(
 * 				final TypeAdapter&lt;E&gt; elementAdapter) {
 * 			return new TypeAdapter&lt;Multiset&lt;E&gt;&gt;() {
 * 				public void write(JsonWriter out, Multiset&lt;E&gt; value)
 * 						throws IOException {
 * 					if (value == null) {
 * 						out.nullValue();
 * 						return;
 * 					}
 *
 * 					out.beginArray();
 * 					for (Multiset.Entry&lt;E&gt; entry : value.entrySet()) {
 * 						out.value(entry.getCount());
 * 						elementAdapter.write(out, entry.getElement());
 * 					}
 * 					out.endArray();
 * 				}
 *
 * 				public Multiset&lt;E&gt; read(JsonReader in) throws IOException {
 * 					if (in.peek() == JsonToken.NULL) {
 * 						in.nextNull();
 * 						return null;
 * 					}
 *
 * 					Multiset&lt;E&gt; result = LinkedHashMultiset.create();
 * 					in.beginArray();
 * 					while (in.hasNext()) {
 * 						int count = in.nextInt();
 * 						E element = elementAdapter.read(in);
 * 						result.add(element, count);
 * 					}
 * 					in.endArray();
 * 					return result;
 * 				}
 * 			};
 * 		}
 * 	}
 * }
 * </pre>
 *
 * Delegating from one type adapter to another is extremely powerful; it's the
 * foundation of how Gson converts Java objects and collections. Whenever
 * possible your factory should retrieve its delegate type adapter in the
 * {@code create()} method; this ensures potentially-expensive type adapter
 * creation happens only once.
 *
 * @since 2.1
 */
public interface TypeAdapterFactory {

	/**
	 * Returns a type adapter for {@code type}, or null if this factory doesn't
	 * support {@code type}.
	 */
	<T> TypeAdapter<T> create(Gson gson, TypeToken<T> type);
}
