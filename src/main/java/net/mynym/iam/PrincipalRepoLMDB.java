package net.mynym.iam;

import static java.nio.ByteBuffer.allocateDirect;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.assertNull;
import static org.lmdbjava.DbiFlags.MDB_CREATE;
import static org.lmdbjava.Env.create;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.lmdbjava.CursorIterator;
import org.lmdbjava.CursorIterator.KeyVal;
import org.lmdbjava.Dbi;
import org.lmdbjava.Env;
import org.lmdbjava.KeyRange;
import org.lmdbjava.Txn;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PrincipalRepoLMDB implements PrincipalRepo {
	public static final ObjectMapper mapper = new ObjectMapper();
	private static final String DB_NAME = "PrincipalRepo";
	final File path = new File("src\\test\\resources");
	final Env<ByteBuffer> env = create().setMapSize(1000 * 1000).setMaxDbs(1).open(path);
	final Dbi<ByteBuffer> db = env.openDbi(DB_NAME, MDB_CREATE);
	final ByteBuffer key = allocateDirect(env.getMaxKeySize());
	final ByteBuffer val = allocateDirect(700);
	private Searcher searcher;

	@Override
	public void put(Principal toPut) {
		key.clear();
		key.putInt(toPut.id).flip();
		val.clear();
		String json = toJson(toPut);
		val.put(json.getBytes(UTF_8)).flip();
		db.put(key, val);
		try {
			searcher.w.addDocument(toPut.toLuceneDoc());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public Principal get(Integer toGet) {
		try (Txn<ByteBuffer> txn = env.txnRead()) {
			key.clear();
			key.putInt(toGet).flip();
			final ByteBuffer found = db.get(txn, key);
			String json = UTF_8.decode(found).toString();
			Principal p = mapper.readValue(json, Principal.class);
			return p;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public void remove(Principal toRemove) {
		// We can also delete. The simplest way is to let Dbi allocate a new Txn...
		db.delete(key);

		// Now if we try to fetch the deleted row, it won't be present
		try (Txn<ByteBuffer> txn = env.txnRead()) {
			assertNull(db.get(txn, key));
		}

	}

	public String toJson(Principal toWrite) {
		try {
			return mapper.writer().writeValueAsString(toWrite);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
	}

	@JsonIgnore
	@Override
	public void setSearcher(Searcher s) {
		searcher = s;

	}

	@JsonIgnore
	@Override
	public Searcher getSearcher() {
		return searcher;
	}

	@Override
	public void exportToJson(FileWriter out) throws IOException {
		try (Txn<ByteBuffer> txn = env.txnRead(); CursorIterator<ByteBuffer> it = db.iterate(txn, KeyRange.all());) {
			Boolean firstObject = true;
			out.write("{\n  \"repository\" : {\n");
			for (final KeyVal<ByteBuffer> kv : it.iterable()) {
				if (firstObject) {
					firstObject = false;
				} else {
					out.write(",\n");
				}
				out.write("    \"" + kv.key().getInt() + "\" : " + UTF_8.decode(kv.val()).toString());
			}
			out.write("\n   }\n}");
		}
	}

	@Override
	public void importFromJson(FileReader in) throws IOException {
		try (Txn<ByteBuffer> txn = env.txnWrite()) {
			BufferedReader reader = new BufferedReader(in);
			int code;
			StringBuilder line = new StringBuilder();
			Boolean inHeader = true;
			int parenDepth = 0;
			while ((code = reader.read()) !=-1) {
				char ch = (char) code;
				if (inHeader) {
					if (ch != '{') {
						continue;
					}
					if (parenDepth < 2) {
						parenDepth++;
						continue;
					} else {
						inHeader = false;
						continue;
					}
				}
				
			}
		}

	}

}
