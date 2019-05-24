package net.mynym.iam;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public interface PrincipalRepo {

	void put(Principal toAdd);

	Principal get(Integer toGet);

	void remove(Principal toRemove);
	
	void setSearcher(Searcher s);
	
	Searcher getSearcher();
	
	void exportToJson(FileWriter out) throws JsonGenerationException, JsonMappingException, IOException;
	
	void importFromJson(FileReader in) throws JsonParseException, JsonMappingException, IOException;
}
