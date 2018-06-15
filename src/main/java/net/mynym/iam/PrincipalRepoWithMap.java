package net.mynym.iam;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PrincipalRepoWithMap implements PrincipalRepo {
	public Map<Integer, Principal> repository = new HashMap<>();

	public void put(Principal toPut) {
		repository.put(toPut.id, toPut);
	}

	public Principal get(Integer toGet) {
		return repository.get(toGet);
	}

	public void remove(Principal toRemove) {
		repository.remove(toRemove.id);
	}

	public Set<Integer> keySet() {
		return repository.keySet();
	}

	@Override
	public void setSearcher(Searcher s) {
		// TODO Auto-generated method stub

	}

	@JsonIgnore
	@Override
	public Searcher getSearcher() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void exportToJson(FileWriter out) throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.writer().withDefaultPrettyPrinter().writeValue(out, this);
	}

	@Override
	public void importFromJson(FileReader in) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		PrincipalRepoWithMap temp = mapper.readValue(in, PrincipalRepoWithMap.class);
		this.repository = temp.repository;
	}
}
