package net.mynym.iam;

import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

public class Searcher {
	StandardAnalyzer analyzer;
	IndexWriterConfig config;
	Directory index;
	IndexWriter w;

	public Searcher() {
		analyzer = new StandardAnalyzer();
		config = new IndexWriterConfig(analyzer);
		index = new RAMDirectory();
		try {
			w = new IndexWriter(index, config);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
